package ru.startandroid.develop.sprint8v3.library.data

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistDbConvertor
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.track.TrackToPlDbConvertor
import ru.startandroid.develop.sprint8v3.library.db.track.TrackToPlEntity
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackDbConvertor: TrackToPlDbConvertor,
    private val context: Context
) : PlaylistRepository {
    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        appDatabase.playlistDao().insertPlaylist(
            PlaylistEntity(
                name = playlistName,
                description = playlistDescription,
                imagePath = playlistImage,
                tracks = "",
                tracksCount = 0
            )
        )
    }

    override fun saveImageToPrivateStorage(bitmap: Bitmap, fileName: String): Boolean {
        if (fileName.isEmpty()) return false
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cache")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)
        val outputStream = FileOutputStream(file)
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist?> = flow {
        if (playlistId < 0) {
            throw IllegalArgumentException("Invalid Playlist ID: $playlistId")
        }
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        if (playlist == null) {
            throw IllegalArgumentException("Playlist with ID $playlistId not found")
        }
        emit(playlistDbConvertor.map(playlist))
    }

    override fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(
            playlistDbConvertor.map(playlist)
        )
    }

    override fun getAllTracks(playlistId: Int): Flow<List<Track>?> = flow {
        val jsonTracks = appDatabase.playlistDao().getAllTracksFromPlaylist(playlistId)
        if (jsonTracks.isNotEmpty()) {
            val tracksIDs = playlistDbConvertor.createTracksFromJson(jsonTracks)
            val tracksInPlaylist = appDatabase.playlistDao().getTrackByIds(tracksIDs).reversed()
            emit(convertFromTrackEntity(tracksInPlaylist))
        } else {
            emit(emptyList())
        }
    }

    private fun convertFromTrackEntity(tracksEntity: List<TrackToPlEntity>): List<Track> {
        return tracksEntity.map { track -> trackDbConvertor.map(track) }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getAllPlaylists()
            emit(convertFromPlaylistEntity(playlists))
        }
    }

    suspend fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { track ->
            playlistDbConvertor.map(track)
        }
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val jsonTracks = playlist.tracks
        appDatabase.playlistDao().deletePlaylist(playlistId)
        if (jsonTracks.isNotEmpty()) {
            playlistDbConvertor.createTracksFromJson(jsonTracks).forEach { trackId ->
                checkTrackInPlaylists(trackId)
            }
        }
    }

    override fun addToPlaylist(track: Track, playlistId: Int): Boolean {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val jsonTracks = playlist.tracks
        var tracks = ArrayList<String>()
        if (jsonTracks.isNotEmpty())
            tracks = playlistDbConvertor.createTracksFromJson(jsonTracks)


        val currentTrack = tracks.filter { _trackId -> _trackId == track.trackId }
        if (currentTrack.isEmpty()) {
            tracks.add(track.trackId)
            appDatabase.playlistDao().updatePlaylists(
                playlist.copy(
                    tracks = playlistDbConvertor.createJsonFromTracks(tracks),
                    tracksCount = tracks.size
                )
            )
            appDatabase.playlistDao().insertTrack(trackDbConvertor.map(track))
            return true
        }
        return false
    }

    override fun deleteFromPlaylist(trackId: String, playlistId: Int) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val jsonTracks = playlist.tracks
        if (jsonTracks.isNotEmpty()) {
            val tracks = playlistDbConvertor.createTracksFromJson(jsonTracks)
            val currentTrack = tracks.filter { track -> track == trackId }
            if (currentTrack.isNotEmpty()) {
                tracks.remove(trackId)
                appDatabase.playlistDao().updatePlaylists(
                    playlist.copy(
                        tracks = playlistDbConvertor.createJsonFromTracks(tracks),
                        tracksCount = tracks.size
                    )
                )
            }
        }
    }

    override suspend fun removeFromPlaylist(trackId: String, playlistId: Int) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val jsonTracks = playlist.tracks
        if (jsonTracks.isNotEmpty()) {
            val tracks = playlistDbConvertor.createTracksFromJson(jsonTracks)
            val currentTrack = tracks.filter { track -> track == trackId }
            if (currentTrack.isNotEmpty()) {
                tracks.remove(trackId)
                appDatabase.playlistDao().updatePlaylist(
                    playlist.copy(
                        tracks = playlistDbConvertor.createJsonFromTracks(tracks),
                        tracksCount = tracks.size
                    )
                )
                checkTrackInPlaylists(trackId)
            }
        }
    }

    suspend fun checkTrackInPlaylists(trackID: String) {
        var inPlaylist = false
        getPlaylists().collect { playlist ->
            for (item in playlist)
                if (item.tracks.contains(trackID)) {
                    inPlaylist = true
                    break
                }
        }
        if (!inPlaylist)
            appDatabase.playlistDao().deleteTrack(trackID)
    }


}
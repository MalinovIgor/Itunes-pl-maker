package ru.startandroid.develop.sprint8v3.library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistDbConvertor
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
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

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getAllPlaylists()
            emit(convertFromPlaylistEntity(playlists))
        }
    }

    suspend fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { track -> playlistDbConvertor.map(track)
        }
    }

    override suspend fun deletePlaylist(playlist: PlaylistEntity) {
        TODO("Not yet implemented")
    }

    override fun addToPlaylist(trackId: String, playlistId: Int) : Boolean {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val jsonTracks = playlist.tracks
        var tracks = ArrayList<String>()
        if (jsonTracks.isNotEmpty())
            tracks = playlistDbConvertor.createTracksFromJson(jsonTracks)


        val currentTrack = tracks.filter { track -> track == trackId }
        if (currentTrack.isEmpty()) {
            tracks.add(trackId)
            appDatabase.playlistDao().updatePlaylists(
                playlist.copy(
                    tracks = playlistDbConvertor.createJsonFromTracks(tracks),
                    tracksCount = tracks.size
                )
            )
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


}
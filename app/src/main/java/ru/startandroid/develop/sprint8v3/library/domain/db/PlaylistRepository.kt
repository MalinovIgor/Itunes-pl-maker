package ru.startandroid.develop.sprint8v3.library.domain.db

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.io.InputStream

interface PlaylistRepository {
    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?,
    )

    fun getPlaylistById(playlistId: Int): Flow<Playlist?>
    fun getAllTracks(playlistId: Int): Flow<List<Track>?>
    suspend fun deletePlaylist(playlistId: Int)
    fun addToPlaylist(track: Track, playlistId: Int) : Boolean
    fun deleteFromPlaylist(trackId: String, playlistId: Int)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun removeFromPlaylist(trackId: String, playlistId: Int)
    fun updatePlaylist(playlist: Playlist)
}
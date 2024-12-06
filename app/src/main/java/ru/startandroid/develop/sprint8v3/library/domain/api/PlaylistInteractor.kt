package ru.startandroid.develop.sprint8v3.library.domain.api

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.io.InputStream

interface PlaylistInteractor {
    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
    }

    fun saveImageToPrivateStorage(bitmap: Bitmap, fileName: String): Boolean
    fun getPlaylistById(playlistId: Int): Flow<Playlist?>
    fun getAllTracks(playlistId: Int): Flow<List<Track>?>
    suspend fun removeFromPlaylist(trackId: String, playlistId: Int)
    fun addToPlaylists(track: Track, playlistId: Int): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
    fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)
}
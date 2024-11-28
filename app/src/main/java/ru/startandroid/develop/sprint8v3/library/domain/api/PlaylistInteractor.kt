package ru.startandroid.develop.sprint8v3.library.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import java.io.InputStream

interface PlaylistInteractor {
    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
    }

    fun addToPlaylists(trackId: String, playlistId: Int): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
}
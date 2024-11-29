package ru.startandroid.develop.sprint8v3.library.domain.api

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
    fun getPlaylistById(playlistId: Int): Flow<Playlist?>
    fun getAllTracks(playlistId: Int): Flow<List<Track>?>

    fun addToPlaylists(track: Track, playlistId: Int): Boolean
    fun getPlaylists(): Flow<List<Playlist>>
}
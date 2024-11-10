package ru.startandroid.develop.sprint8v3.library.domain.db

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface PlaylistRepository {
    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?,
    )

    suspend fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun deletePlaylist(playlist: PlaylistEntity) //@TODO
    suspend fun addToPlaylist()  //@TODO
    suspend fun deleteFromPlaylist()  //@TODO


}
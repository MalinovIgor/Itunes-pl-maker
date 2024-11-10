package ru.startandroid.develop.sprint8v3.library.data

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.track.TrackDbConvertor
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository

class PlaylistRepositoryImpl (private val appDatabase: AppDatabase,
): PlaylistRepository {
    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {

    }

    override suspend fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(playlist: PlaylistEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun addToPlaylist() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromPlaylist() {
        TODO("Not yet implemented")
    }
}
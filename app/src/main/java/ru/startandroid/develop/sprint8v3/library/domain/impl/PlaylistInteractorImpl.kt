package ru.startandroid.develop.sprint8v3.library.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist

class PlaylistInteractorImpl (private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        repository.createPlaylist(playlistName,playlistDescription,playlistImage)
    }

    override fun addToPlaylists(trackId: String, playlistId: Int) : Boolean {
        return repository.addToPlaylist(trackId, playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()    }
}
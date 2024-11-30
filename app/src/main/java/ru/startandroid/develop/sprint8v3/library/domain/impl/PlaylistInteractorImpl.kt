package ru.startandroid.develop.sprint8v3.library.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        repository.createPlaylist(playlistName, playlistDescription, playlistImage)
    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist?> =
        repository.getPlaylistById(playlistId)


    override fun getAllTracks(playlistId: Int): Flow<List<Track>?> =
        repository.getAllTracks(playlistId)

    override suspend fun removeFromPlaylist(trackId: String, playlistId: Int) {
        repository.removeFromPlaylist(trackId, playlistId)
    }

    override fun addToPlaylists(track: Track, playlistId: Int): Boolean {
        return repository.addToPlaylist(track, playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist( playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

}
package ru.startandroid.develop.sprint8v3.library.domain.impl

import android.util.Log
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository

class PlaylistInteractorImpl (private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        repository.createPlaylist(playlistName,playlistDescription,playlistImage)
    }
}
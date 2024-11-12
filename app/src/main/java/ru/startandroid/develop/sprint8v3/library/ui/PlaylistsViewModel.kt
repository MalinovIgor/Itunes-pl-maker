package ru.startandroid.develop.sprint8v3.library.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor,
    private val application: Application
) : ViewModel() {

    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {
        interactor.createPlaylist(playlistName,playlistDescription,playlistImage)
    }

}
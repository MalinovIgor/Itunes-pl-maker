package ru.startandroid.develop.sprint8v3.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivityViewModel

class PlayerViewModelFactory(private val playerInteractor: PlayerInteractor) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerActivityViewModel::class.java)) {
            return PlayerActivityViewModel(playerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
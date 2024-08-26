package ru.startandroid.develop.sprint8v3.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState

class PlayerActivityViewModel(private val interactor: PlayerInteractor) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> get() = _currentTime

    init {
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    fun play() {
        interactor.play()
        _playerState.value = PlayerState.STATE_PLAYING
    }

    fun pause() {
        interactor.pause()
        _playerState.value = PlayerState.STATE_PAUSED
    }

    fun stop() {
        interactor.stop()
        _playerState.value = PlayerState.STATE_STOPPED
    }

    fun prepare(track: Track) {
        interactor.prepare(track)
        _playerState.value = PlayerState.STATE_PREPARED
    }

    fun updateCurrentTime(time: Long) {
        _currentTime.value = time
    }
}
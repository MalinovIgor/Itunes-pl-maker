package ru.startandroid.develop.sprint8v3.player.domain.impl

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track


class PlayerInteractorImpl(private var mediaPlayer: MediaPlayer,
                           ) : PlayerInteractor {

    private val _playerState = MutableLiveData<PlayerState>().apply {
        value = PlayerState.STATE_DEFAULT
    }
    override val playerState: LiveData<PlayerState> get() = _playerState
    init {
        mediaPlayer.setOnCompletionListener {
            _playerState.postValue(PlayerState.STATE_COMPLETED)
        }
    }

    override fun play() {
        mediaPlayer.start()
        _playerState.postValue(PlayerState.STATE_PLAYING)
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _playerState.postValue(PlayerState.STATE_PAUSED)
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        _playerState.postValue(PlayerState.STATE_STOPPED)
        _playerState.postValue(PlayerState.STATE_DEFAULT)
    }

    override fun prepare(track: Track) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track.previewUrl.toString())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState.postValue(PlayerState.STATE_PREPARED)
        }
        _playerState.postValue(PlayerState.STATE_PREPARED)
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}
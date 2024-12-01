package ru.startandroid.develop.sprint8v3.player.domain.impl

import android.media.MediaPlayer
import android.util.Log
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState


class PlayerInteractorImpl(
    private var mediaPlayer: MediaPlayer,
    private val trackUrl : String,
) : PlayerInteractor {

        init {
            if (trackUrl.isBlank()) {
                throw IllegalArgumentException("Track URL cannot be blank")
            }
            Log.d("PlayerInteractorImpl", "Initialized with trackUrl: $trackUrl")
    }

    private var state: PlayerState = PlayerState.STATE_DEFAULT

    override fun play() {
        mediaPlayer.start()
        state = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = PlayerState.STATE_PAUSED
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        state = PlayerState.STATE_STOPPED
    }

    override fun prepare() {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            state = PlayerState.STATE_COMPLETED
        }
    }

    override fun getState():PlayerState{
        return state
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}
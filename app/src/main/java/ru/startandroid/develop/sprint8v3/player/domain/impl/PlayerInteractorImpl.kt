package ru.startandroid.develop.sprint8v3.player.domain.impl

import android.media.MediaPlayer
import android.util.Log
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track


class PlayerInteractorImpl(private var mediaPlayer: MediaPlayer,
                           ) : PlayerInteractor {

    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private var currentPosition: Int = 0

    override fun play() {
        mediaPlayer?.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                currentPosition = it.currentPosition
                playerState = PlayerState.STATE_PAUSED
            }
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        playerState = PlayerState.STATE_STOPPED
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun prepare(track: Track) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer.reset()
            Log.e("interactorCheckMP=!null","$mediaPlayer")
        }
        mediaPlayer.setDataSource(track.previewUrl.toString())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        playerState = PlayerState.STATE_PREPARED
    }

    override fun getCurrentTime(): Int {
            return mediaPlayer?.currentPosition ?:0
    }

}
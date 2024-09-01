package ru.startandroid.develop.sprint8v3.player.domain.impl

import android.media.MediaPlayer
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState


class PlayerInteractorImpl(private val mediaPlayer: MediaPlayer,
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
        mediaPlayer.release()
        playerState = PlayerState.STATE_STOPPED
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun prepare(track: Track) {
        mediaPlayer?.setDataSource(track.previewUrl.toString())
        mediaPlayer?.prepareAsync()
        playerState = PlayerState.STATE_PREPARED
        mediaPlayer?.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun getCurrentTime(): Int {
            return mediaPlayer?.currentPosition ?:0
    }

}
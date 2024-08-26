package ru.startandroid.develop.sprint8v3.player.domain.impl

import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.domain.repository.PlayerRepository
import ru.startandroid.develop.sprint8v3.player.state.PlayerState


class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    private lateinit var track: Track
    private val timerRunnable = timer()

    override fun loadTrackInfo(intent: Intent, consumer: PlayerInteractor.TracksConsumer) {
        val track = repository.getTrackFromIntent(intent)
        if (track != null) {
            this.track = track
            consumer.consume(track)
        } else {
            consumer.onError(Throwable("Track not found"))
        }
    }

    override fun play() {
        mediaPlayer?.start()
        playerState = PlayerState.STATE_PLAYING
        handler.post(timerRunnable)
    }

    override fun pause() {
        mediaPlayer?.pause()
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(timerRunnable)
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = PlayerState.STATE_STOPPED
        handler.removeCallbacks(timerRunnable)
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun prepare(track: Track) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.setDataSource(track.previewUrl.toString())
        mediaPlayer?.prepareAsync()
        playerState = PlayerState.STATE_PREPARED
        mediaPlayer?.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    private fun timer(): Runnable {
        return Runnable {
            handler.postDelayed(timerRunnable, TIMER_UPDATE_DELAY)
        }
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 500L
    }
}
package ru.startandroid.develop.sprint8v3.player.domain.api

import android.content.Intent
import ru.startandroid.develop.sprint8v3.domain.models.Track

interface PlayerInteractor {
    fun loadTrackInfo(intent: Intent, consumer: TracksConsumer)
    fun play()
    fun pause()
    fun stop()
    fun prepare(track:Track)


    interface TracksConsumer {
        fun consume(track:Track)
        fun onError(error: Throwable)
    }
}
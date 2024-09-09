package ru.startandroid.develop.sprint8v3.player.domain.api

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface PlayerInteractor {
    fun play()
    fun pause()
    fun stop()
    fun prepare(track: Track)
    fun getCurrentTime(): Int

}
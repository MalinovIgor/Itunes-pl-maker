package ru.startandroid.develop.sprint8v3.player.domain.api

import androidx.lifecycle.LiveData
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface PlayerInteractor {
    val playerState: LiveData<PlayerState>
    fun play()
    fun pause()
    fun stop()
    fun prepare(track: Track)
    fun getCurrentTime(): Int

}
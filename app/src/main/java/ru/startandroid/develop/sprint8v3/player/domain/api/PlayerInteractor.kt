package ru.startandroid.develop.sprint8v3.player.domain.api

import ru.startandroid.develop.sprint8v3.player.state.PlayerState

interface PlayerInteractor {
    fun play()
    fun getState():PlayerState
    fun pause()
    fun stop()
    fun prepare()
    fun getCurrentTime(): Int
}
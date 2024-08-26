package ru.startandroid.develop.sprint8v3.player.state

sealed interface PlayerState {
    data object STATE_DEFAULT : PlayerState
    data object STATE_PREPARED : PlayerState
    data object STATE_PLAYING : PlayerState
    data object STATE_PAUSED : PlayerState
    data object STATE_STOPPED : PlayerState

}
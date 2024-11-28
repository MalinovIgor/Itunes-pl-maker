package ru.startandroid.develop.sprint8v3.player.state

import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist

class IsInPlaylistState(
    val isInPlaylist: Boolean,
    val playlist: Playlist
) {
}
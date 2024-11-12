package ru.startandroid.develop.sprint8v3.library.domain.api

interface PlaylistInteractor {
    suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        playlistImage: String?
    ) {

    }
}
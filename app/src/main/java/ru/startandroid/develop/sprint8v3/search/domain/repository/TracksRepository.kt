package ru.startandroid.develop.sprint8v3.search.domain.repository

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}
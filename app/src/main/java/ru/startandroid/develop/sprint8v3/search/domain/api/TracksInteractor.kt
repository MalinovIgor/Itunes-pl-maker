package ru.startandroid.develop.sprint8v3.search.domain.api

import ru.startandroid.develop.sprint8v3.search.domain.models.Resource
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
        fun onError(error: Throwable)
    }
}
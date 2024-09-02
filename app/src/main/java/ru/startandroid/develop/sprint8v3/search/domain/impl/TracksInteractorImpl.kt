package ru.startandroid.develop.sprint8v3.search.domain.impl

import ru.startandroid.develop.sprint8v3.search.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.search.domain.models.Resource
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.domain.repository.TracksRepository
import java.util.concurrent.Executors
    class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
        private val executor = Executors.newCachedThreadPool()

        override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
            executor.execute {
                try {
                    val tracks = repository.searchTracks(expression)
                    val resource = Resource.Success(tracks)
                    consumer.consume(resource)
                } catch (e: Exception) {
                    val resource = Resource.Error<List<Track>>(e.toString())
                    consumer.consume(resource)
                }
            }
        }
    }
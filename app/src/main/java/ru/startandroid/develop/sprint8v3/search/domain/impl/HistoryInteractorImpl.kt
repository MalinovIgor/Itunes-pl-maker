package ru.startandroid.develop.sprint8v3.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.search.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.domain.repository.SearchHistoryRepository

class HistoryInteractorImpl(private val repository: SearchHistoryRepository) : HistoryInteractor {
    override suspend fun addToHistory(track: Track) {
        repository.addToHistory(track)
        }

    override fun isHistoryEmpty(): Boolean =
        repository.isHistoryEmpty()

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun loadHistoryTracks(): Flow<ArrayList<Track>>  {
        return repository.loadHistoryTracks()
    }
}
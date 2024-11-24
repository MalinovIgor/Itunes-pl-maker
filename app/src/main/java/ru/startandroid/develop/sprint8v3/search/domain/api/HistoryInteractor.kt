package ru.startandroid.develop.sprint8v3.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface HistoryInteractor {
    fun clearHistory()
    fun loadHistoryTracks(): Flow<ArrayList<Track>>
    suspend fun addToHistory(track: Track)

    fun isHistoryEmpty():Boolean
}
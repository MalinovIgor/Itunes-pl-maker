package ru.startandroid.develop.sprint8v3.search.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface SearchHistoryRepository {
    fun clearHistory()

    suspend fun addToHistory(track: Track)
    fun loadHistoryTracks() : Flow<ArrayList<Track>>
    fun saveHistoryTracks(tracks: ArrayList<Track>)
    fun isHistoryEmpty(): Boolean
}
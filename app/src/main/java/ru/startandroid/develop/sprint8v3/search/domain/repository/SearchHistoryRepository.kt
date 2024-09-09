package ru.startandroid.develop.sprint8v3.search.domain.repository

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface SearchHistoryRepository {
    fun clearHistory()

    fun addToHistory(track: Track)
    fun loadHistoryTracks() : ArrayList<Track>
    fun saveHistoryTracks(tracks: ArrayList<Track>)
    fun isHistoryEmpty(): Boolean
}
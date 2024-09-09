package ru.startandroid.develop.sprint8v3.search.domain.api

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface HistoryInteractor {
    fun clearHistory()
    fun loadHistoryTracks(): ArrayList<Track>
    fun addToHistory(track: Track)

    fun isHistoryEmpty():Boolean
}
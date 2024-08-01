package ru.startandroid.develop.sprint8v3.domain.impl

import android.util.Log
import ru.startandroid.develop.sprint8v3.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.domain.repository.SearchHistoryRepository

class HistoryInteractorImpl(private val repository: SearchHistoryRepository) : HistoryInteractor {
    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
        }

    override fun isHistoryEmpty(): Boolean =
        repository.isHistoryEmpty()

//    override fun saveHistoryTracks(tracks: ArrayList<Track>) {
//        repository.saveHistoryTracks(tracks)
//    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun loadHistoryTracks(): ArrayList<Track> {
        Log.d("HistoryInteractorImpl", "Loaded history tracks: ${repository.loadHistoryTracks().size} items")
        return repository.loadHistoryTracks()
    }
}
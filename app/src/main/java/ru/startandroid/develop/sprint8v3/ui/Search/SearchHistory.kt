package ru.startandroid.develop.sprint8v3.ui.Search

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.Observable
import ru.startandroid.develop.sprint8v3.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.domain.models.Track
const val trackHistoryString = "trackHistory"
class SearchHistory(private val sharedPreferences: SharedPreferences) : Observable() {
//    private val historyInteractor: HistoryInteractor by lazy { Creator.provideHistoryInteractor() }
//    private val gson = Gson()
//    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type
//
//    fun addToHistory(track: Track) {
//        val trackHistory = historyInteractor.loadHistoryTracks()
//
//        val existingTrackIndex = trackHistory.indexOfFirst { it.trackId == track.trackId }
//        if (existingTrackIndex != -1) {
//            trackHistory.removeAt(existingTrackIndex)
//        }
//
//        trackHistory.add(0, track)
//
//        val MAX_HISTORY_SIZE = 10
//        if (trackHistory.size > MAX_HISTORY_SIZE) {
//            trackHistory.removeLast()
//        }
//
//        historyInteractor.saveHistoryTracks(trackHistory)
//        notifyObservers()
//    }
//
//    fun clearHistory() {
//        historyInteractor.clearHistory()
//        notifyObservers()
//    }
//
//    fun loadHistoryTracks(): ArrayList<Track> {
//        return historyInteractor.loadHistoryTracks()
//    }
//
//    fun isHistoryEmpty(): Boolean {
//        return historyInteractor.loadHistoryTracks().isEmpty()    }
}
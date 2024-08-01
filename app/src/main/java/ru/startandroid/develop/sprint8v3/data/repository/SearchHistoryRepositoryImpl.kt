package ru.startandroid.develop.sprint8v3.data.repository

import android.content.SharedPreferences
import android.util.Log
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {
    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type


    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    override fun addToHistory(track: Track) {
        val history = loadHistoryTracks()
        history.add(0, track)

        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }
        saveHistoryTracks(history)
    }

    override fun loadHistoryTracks() : ArrayList<Track> {
        val trackHistoryJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        val historyTracks = ArrayList<Track>()
        if (trackHistoryJson != null) {
            historyTracks.addAll(gson.fromJson(trackHistoryJson, trackListType))
        }
        Log.d("SearchHistoryRepositoryImpl", "Loaded history tracks: ${historyTracks.size} items")
        return historyTracks
    }

    override fun saveHistoryTracks(tracks: ArrayList<Track>) {
        val trackHistoryJson = gson.toJson(tracks)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, trackHistoryJson).apply()
    }

    override fun isHistoryEmpty() : Boolean {
        return sharedPreferences.getString(SEARCH_HISTORY_KEY, null)?.isEmpty() ?: true
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val SEARCH_HISTORY_SHARED_PREFERENCES = "search_history"
    }
}
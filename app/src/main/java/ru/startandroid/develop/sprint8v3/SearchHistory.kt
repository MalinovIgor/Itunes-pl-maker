package ru.startandroid.develop.sprint8v3

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val sharedPrefsHistory = "sharedPrefsHistory"
const val EDIT_TEXT_KEY = "key_for_edit_text"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type

    fun addToHistory(track: Track) {
        val trackHistoryJson = sharedPreferences.getString("trackHistory", null)
        val trackHistory = mutableListOf<Track>()

        if (trackHistoryJson != null) {
            trackHistory.addAll(gson.fromJson(trackHistoryJson, trackListType))

            val existingTrackIndex = trackHistory.indexOfFirst { it.trackId == track.trackId }
            if (existingTrackIndex != -1) {
                trackHistory.removeAt(existingTrackIndex)
            }

            trackHistory.add(0, track)
        } else {
            trackHistory.add(track)
        }

        val MAX_HISTORY_SIZE = 10
        if (trackHistory.size > MAX_HISTORY_SIZE) {
            trackHistory.removeLast()
        }

        val updatedTrackHistoryJson = gson.toJson(trackHistory)
        sharedPreferences.edit().putString("trackHistory", updatedTrackHistoryJson).apply()
    }

    fun clearHistory() {
        sharedPreferences.edit().putString("trackHistory", null).apply()
    }

    fun loadHistoryTracks(): ArrayList<Track> {
        val trackHistoryJson = sharedPreferences.getString("trackHistory", null)
        val historyTracks = ArrayList<Track>()
        if (trackHistoryJson != null) {
            historyTracks.addAll(gson.fromJson(trackHistoryJson, trackListType))
        }
        return historyTracks
    }
}
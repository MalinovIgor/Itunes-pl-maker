package ru.startandroid.develop.sprint8v3.ui

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.startandroid.develop.sprint8v3.Observable
import ru.startandroid.develop.sprint8v3.data.dto.TrackDto
import ru.startandroid.develop.sprint8v3.domain.models.Track
const val trackHistoryString = "trackHistory"
class SearchHistory(private val sharedPreferences: SharedPreferences) : Observable() {

    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<TrackDto>>() {}.type

    fun addToHistory(track: TrackDto) {
        val trackHistoryJson = sharedPreferences.getString(trackHistoryString, null)
        val trackHistory = mutableListOf<TrackDto>()

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
        sharedPreferences.edit().putString(trackHistoryString, updatedTrackHistoryJson).apply()

        notifyObservers()
    }

    fun clearHistory() {
        sharedPreferences.edit().putString(trackHistoryString, null).apply()
        notifyObservers()
    }

    fun loadHistoryTracks(): ArrayList<TrackDto> {
        val trackHistoryJson = sharedPreferences.getString(trackHistoryString, null)
        val historyTracks = ArrayList<TrackDto>()
        if (trackHistoryJson != null) {
            historyTracks.addAll(gson.fromJson(trackHistoryJson, trackListType))
        }
        return historyTracks
    }

    fun isHistoryEmpty(): Boolean {
        return this.loadHistoryTracks().isEmpty()
    }
}
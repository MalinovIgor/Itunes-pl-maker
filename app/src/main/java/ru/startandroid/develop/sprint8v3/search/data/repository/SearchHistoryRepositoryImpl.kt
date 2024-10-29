package ru.startandroid.develop.sprint8v3.search.data.repository

import android.content.SharedPreferences
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.library.data.InFavoritesCheckRepository
import ru.startandroid.develop.sprint8v3.library.db.AppDatabase

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val inFavoritesCheckRepository: InFavoritesCheckRepository
) :
    SearchHistoryRepository {
    private val gson = Gson()
    private val trackListType = object : TypeToken<ArrayList<Track>>() {}.type

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_KEY).apply()
    }

    override suspend fun addToHistory(track: Track) {
        val history = loadHistoryTracks().first()

        val existingTrackIndex = history.indexOf(track)
        if (existingTrackIndex != -1) {
            history.removeAt(existingTrackIndex)
        }
        history.add(0, track)

        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        saveHistoryTracks(history)
    }

    override fun loadHistoryTracks(): Flow<ArrayList<Track>> = flow {
        val trackHistoryJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        val historyTracks = ArrayList<Track>()
        if (trackHistoryJson != null) {
            historyTracks.addAll(gson.fromJson(trackHistoryJson, trackListType))
            historyTracks.forEach {
                it.isFavorites = inFavoritesCheckRepository.isInFavorites(it.trackId)
            }
        }
        emit(historyTracks)
    }

    override fun saveHistoryTracks(tracks: ArrayList<Track>) {
        val trackHistoryJson = gson.toJson(tracks)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, trackHistoryJson).apply()
    }

    override fun isHistoryEmpty(): Boolean {
        return sharedPreferences.getString(SEARCH_HISTORY_KEY, null)?.isEmpty() ?: true
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
    }
}
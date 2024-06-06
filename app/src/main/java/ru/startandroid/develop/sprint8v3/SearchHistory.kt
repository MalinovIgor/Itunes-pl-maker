import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.startandroid.develop.sprint8v3.Observable
import ru.startandroid.develop.sprint8v3.Track

class SearchHistory(private val sharedPreferences: SharedPreferences) : Observable() {

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

        notifyObservers()
    }

    fun clearHistory() {
        sharedPreferences.edit().putString("trackHistory", null).apply()
        notifyObservers()
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
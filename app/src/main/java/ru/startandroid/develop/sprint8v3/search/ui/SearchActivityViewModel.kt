package ru.startandroid.develop.sprint8v3.search.ui

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivity
import ru.startandroid.develop.sprint8v3.player.ui.SELECTEDTRACK
import ru.startandroid.develop.sprint8v3.search.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.search.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.search.domain.models.Resource
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.ui.fragment.SearchFragment
import ru.startandroid.develop.sprint8v3.search.utils.debounce

class SearchActivityViewModel(
    application: Application,
    private val tracksInteractor: TracksInteractor,
    private val searchHistorySaver: HistoryInteractor
) :
    AndroidViewModel(application) {

    private var lastSearchedText: String? = null
    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState
    private lateinit var onTextChangedSearchDebounce: (String) -> Unit

    private val handler = Handler(Looper.getMainLooper())

    init {
        val searchHistory = searchHistorySaver.loadHistoryTracks()
        if (searchHistory.isEmpty()) {
            renderState(SearchState.NoTracks)
        } else {
            renderState(SearchState.ContentHistoryTracks(searchHistory))
        }
    }

    public override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun search(query: String) {
        renderState(SearchState.Loading)

        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: Resource<List<Track>>) {
                when (foundTracks) {
                    is Resource.Error -> renderState(
                        SearchState.Error(
                            errorMessage = getApplication<Application>().getString(
                                R.string.connection_trouble
                            )
                        )
                    )

                    is Resource.Success -> {
                        if (foundTracks.data?.isNotEmpty() == true) {
                            renderState(SearchState.ContentFoundTracks(foundTracks.data))
                        } else {
                            renderState(SearchState.NothingFound)
                        }
                    }
                }
            }

            override fun onError(error: Throwable) {
                renderState(SearchState.Error(getApplication<Application>().getString(R.string.connection_trouble)))
            }
        })
    }

    fun loadHistory() {
        val historyTracks = searchHistorySaver.loadHistoryTracks()
        if (historyTracks.isEmpty()) {
            renderState(SearchState.NoTracks)
        } else {
            renderState(SearchState.ContentHistoryTracks(historyTracks))
        }
    }

    fun clearHistory() {
        searchHistorySaver.clearHistory()
    }

    fun searchDebounce(changedText: String) {
        if (lastSearchedText == changedText) {
            Log.d("ifEntered", "xx")

            return
        }

        lastSearchedText = changedText
        onTextChangedSearchDebounce =
            debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, false) { changedText ->
                Log.d("searchDebounceEntered", "xx")

                search(changedText)
            }
        onTextChangedSearchDebounce(changedText)
    }

    private fun renderState(state: SearchState) {
        _searchState.postValue(state)
    }

    fun onClick(track: Track) {
        searchHistorySaver.addToHistory(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2500L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private var textChangedSearchDebounceJob: Job? = null

    init {
        val searchHistory = searchHistorySaver.loadHistoryTracks()
        if (searchHistory.isEmpty()) {
            renderState(SearchState.NoTracks)
        } else {
            renderState(SearchState.ContentHistoryTracks(searchHistory))
        }
    }

    public override fun onCleared() {
        textChangedSearchDebounceJob?.cancel()
    }
    private fun processResult(result: Pair<Resource<List<Track>>?, Throwable?>) {
        val resource = result.first
        val error = result.second

        when (resource) {
            is Resource.Error -> {
                renderState(
                    SearchState.Error(
                        errorMessage = error?.message ?: getApplication<Application>().getString(R.string.connection_trouble)
                    )
                )
            }

            is Resource.Success -> {
                val foundTracks = resource.data
                if (!foundTracks.isNullOrEmpty()) {
                    renderState(SearchState.ContentFoundTracks(foundTracks))
                } else {
                    renderState(SearchState.NothingFound)
                }
            }

            null -> {
                renderState(
                    SearchState.Error(
                        errorMessage = getApplication<Application>().getString(R.string.connection_trouble)
                    )
                )
            }
        }
    }
    fun search(query: String) {
        renderState(SearchState.Loading)
        viewModelScope.launch {
            tracksInteractor
                .searchTracks(query)
                .collect { pair -> processResult(pair)
                }
        }
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
            return
        }
        lastSearchedText = changedText

        textChangedSearchDebounceJob?.cancel()

        textChangedSearchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(changedText)
        }
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

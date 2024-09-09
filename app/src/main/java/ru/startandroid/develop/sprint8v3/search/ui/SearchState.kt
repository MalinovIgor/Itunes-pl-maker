package ru.startandroid.develop.sprint8v3.search.ui

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

sealed interface SearchState {
    data object NothingFound : SearchState
    data class Error(val errorMessage: String) : SearchState
    data object Loading : SearchState
    data class ContentFoundTracks(val tracks: List<Track>) : SearchState
    data class ContentHistoryTracks(val historyTracks : List<Track>) : SearchState
    data object NoTracks : SearchState

}
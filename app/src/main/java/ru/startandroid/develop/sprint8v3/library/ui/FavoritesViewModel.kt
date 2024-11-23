package ru.startandroid.develop.sprint8v3.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class FavoritesViewModel (
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val favoriteTracks = MutableLiveData<List<Track>>()
    fun getFavoriteTracks(): LiveData<List<Track>> = favoriteTracks

    fun returnFavoriteTracks() {
        viewModelScope.launch {
            favoritesInteractor.getFavoritesTracks().collect { trackList ->
                favoriteTracks.postValue(trackList)
            }
        }
    }

    init {
        returnFavoriteTracks()
    }
}
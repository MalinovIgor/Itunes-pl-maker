package ru.startandroid.develop.sprint8v3.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class PlaylistViewViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val playlist = MutableLiveData<Playlist?>()
    fun observePlaylist(): LiveData<Playlist?> = playlist

    private val allTracks = MutableLiveData<List<Track>?>()
    fun observeAllTracks(): LiveData<List<Track>?> = allTracks


    fun loadPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getPlaylistById(playlistId).collect {
                if (it != null) {
                    interactor.getAllTracks(it.id).collect() { tracks ->
                        allTracks.postValue(tracks)
                    }
                    playlist.postValue(it)
                }

            }
        }
    }
}
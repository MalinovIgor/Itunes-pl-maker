package ru.startandroid.develop.sprint8v3.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.IsInPlaylistState
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(
    private val interactor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    val playerState = MutableLiveData(PlayerState.STATE_DEFAULT)
    private val isInPlaylistState = MutableLiveData<IsInPlaylistState>()
    fun observeIsInPlaylist():LiveData<IsInPlaylistState> = isInPlaylistState

    private val playlists = MutableLiveData<List<Playlist>>()
    fun observePlaylists () : LiveData<List<Playlist>> = playlists
    val favoritesState = MutableLiveData<Boolean>()
    private var timerJob: Job? = null

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int> get() = _currentTime
    fun observePlayerState(): LiveData<PlayerState> = playerState
    fun observeFavoritesState(): LiveData<Boolean> = favoritesState

    init {
        prepare()
    }

    fun isTrackInFavorites(trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesState.postValue(
                favoritesInteractor.isFavorite(trackId)
            )
        }
    }

    fun onAddToPlaylistClick(track: Track, playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            isInPlaylistState.postValue(
                IsInPlaylistState(
                    playlistInteractor.addToPlaylists(
                        track,
                        playlist.id
                    ), playlist
                )
            )
        }
    }

    fun updatePlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect {
                playlists.postValue(it)
            }
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorites) {
                favoritesInteractor.deleteTrackFromFavorites(track)
                track.isFavorites = false
                favoritesState.postValue(false)
            } else {
                favoritesInteractor.addTrackToFavorites(track)
                track.isFavorites = true
                favoritesState.postValue(true)
            }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(300L)
                _currentTime.postValue(interactor.getCurrentTime())
            }
        }
    }

    fun play() {
        playerState.postValue(PlayerState.STATE_PLAYING)
        interactor.play()
        startTimer()
    }

    fun getState() {
        playerState.postValue(interactor.getState())
    }

    fun pause() {
        playerState.postValue(PlayerState.STATE_PAUSED)
        interactor.pause()
    }

    fun stop() {
        interactor.stop()
        playerState.postValue(PlayerState.STATE_STOPPED)
    }

    fun prepare() {
        interactor.prepare()
        playerState.postValue(PlayerState.STATE_PREPARED)
    }

    fun parseDate(releaseDateString: String?, noData: String): String {
        if (releaseDateString == noData || releaseDateString == null) {
            return noData
        } else {
            return try {
                val releaseDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val date = releaseDateFormat.parse(releaseDateString)
                val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                outputDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                noData
            }
        }
    }

}
package ru.startandroid.develop.sprint8v3.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(private val interactor: PlayerInteractor) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int> get() = _currentTime

    init {
        _playerState.value = PlayerState.STATE_DEFAULT
    }

    fun play() {
        interactor.play()
        _playerState.value = PlayerState.STATE_PLAYING
        startTimer()
    }
    private fun startTimer() {
        handler.post(timerRunnable)
    }
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable by lazy {
        object : Runnable {
            override fun run() {
                if (_playerState.value == PlayerState.STATE_PLAYING) {
                    _currentTime.postValue(interactor.getCurrentTime())
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }
            }
        }
    }
    fun pause() {
        interactor.pause()
        _playerState.value = PlayerState.STATE_PAUSED
        pauseTimer()
    }

    fun stop() {
        interactor.stop()
        _playerState.value = PlayerState.STATE_STOPPED
    }

    fun prepare(track: Track) {
        interactor.prepare(track)
        _playerState.value = PlayerState.STATE_PREPARED
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        pauseTimer()
    }

    fun parseDate(releaseDateString: String?, noData: String): String {
        if (releaseDateString == noData || releaseDateString == null) {
            return noData
        } else {
            return try {
                val releaseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val date = releaseDateFormat.parse(releaseDateString)
                val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                outputDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                noData
            }
        }
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 250L
        fun getViewModelFactory(trackUrl: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PlayerActivityViewModel(
                        interactor = Creator.providePlayerInteractor(),
                    )
                }
            }
    }
}
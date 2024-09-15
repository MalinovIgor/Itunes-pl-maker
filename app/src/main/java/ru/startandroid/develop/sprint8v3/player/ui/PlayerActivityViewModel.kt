package ru.startandroid.develop.sprint8v3.player.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivityViewModel(private val interactor: PlayerInteractor) : ViewModel() {

    val playerState = MutableLiveData(PlayerState.STATE_DEFAULT)

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int> get() = _currentTime
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        prepare()
    }

    fun play() {
        playerState.postValue(PlayerState.STATE_PLAYING)
        interactor.play()
        startTimer()
    }

    fun getState() {
        playerState.postValue(interactor.getState())
    }

    private fun startTimer() {
        handler.post(timerRunnable)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable by lazy {
        object : Runnable {
            override fun run() {

                if (playerState.value == PlayerState.STATE_PLAYING) {

                    _currentTime.postValue(interactor.getCurrentTime())
                    handler.postDelayed(this, TIMER_UPDATE_DELAY)
                }

            }
        }
    }

    fun pause() {
        interactor.pause()
        playerState.postValue(PlayerState.STATE_PAUSED)
        pauseTimer()
    }

    fun stop() {
        interactor.stop()
        playerState.postValue(PlayerState.STATE_STOPPED)
    }

    fun prepare() {
        interactor.prepare()
        playerState.postValue(PlayerState.STATE_PREPARED)
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

    companion object {
        private const val TIMER_UPDATE_DELAY = 250L
    }
}
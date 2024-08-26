package ru.startandroid.develop.sprint8v3.ui.player

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.ActivityPlayerBinding
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.PlayerViewModelFactory
import ru.startandroid.develop.sprint8v3.player.data.repository.PlayerRepositoryImpl
import ru.startandroid.develop.sprint8v3.player.domain.impl.PlayerInteractorImpl
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivityViewModel

const val selectedTrack = "selectedTrack"


class PlayerActivity : AppCompatActivity() {

    private val repository by lazy { PlayerRepositoryImpl() }
    private val interactor by lazy { PlayerInteractorImpl(repository) }
    private lateinit var viewModel: PlayerActivityViewModel
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PlayerActivity", "onCreate called")


        val track = intent.getSerializableExtra(selectedTrack) as? Track

        if (track == null) {
            Log.e("PlayerActivity", "No track found in intent")
            finish()
            return
        }

        viewModel = ViewModelProvider(this, PlayerViewModelFactory(interactor))
            .get(PlayerActivityViewModel::class.java)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadTrackInfo(track)

        setupViews()

        binding.backArrow.setOnClickListener {
            finish()
        }

        viewModel.playerState.observe(this, Observer { state ->
            handlePlayerState(state)
        })

        viewModel.currentTime.observe(this, Observer { time ->
            updateTimer(time)
        })

        viewModel.prepare(track)

        binding.play.setOnClickListener {
            viewModel.play()
        }
    }

    private fun setupViews() {

    }

    private fun loadTrackInfo(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one)))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(binding.artworkImageViewBig)

        binding.trackNameTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        binding.genreTextView.text = track.primaryGenreName ?: noData
        binding.countryTextView.text = track.country ?: noData

        val releaseDateString = track.releaseDate ?: noData
        binding.releaseDateTextView.text = if (releaseDateString == noData) {
            noData
        } else {
            try {
                yearDateFormat.format(releaseDateFormat.parse(releaseDateString))
            } catch (e: Exception) {
                e.printStackTrace()
                noData
            }
        }

        binding.collectionNameTextView.text = track.collectionName
        binding.trackTimeTextView.text = timerDateFormat.format(track.trackTime)
    }

    private fun handlePlayerState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                binding.play.setImageResource(R.drawable.pause)
            }
            PlayerState.STATE_PAUSED, PlayerState.STATE_STOPPED -> {
                binding.play.setImageResource(R.drawable.play)
            }
            else -> {

            }
        }
    }

    private fun updateTimer(time: Long) {
        binding.timer.text = timerDateFormat.format(time)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }

    companion object {
        private const val noData = "отсутствует"
        private val yearDateFormat by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }
        private val releaseDateFormat by lazy {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        }
        private val timerDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    }
}
package ru.startandroid.develop.sprint8v3.player.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.ActivityPlayerBinding
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

const val SELECTEDTRACK = "selectedTrack"

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerActivityViewModel
    private lateinit var binding: ActivityPlayerBinding


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val track = intent.getSerializableExtra(SELECTEDTRACK) as? Track
        outState.putString("PREVIEW_URL", track?.previewUrl)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.timer.text = "00:00"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timer.text = "00:00"
        var previewUrl = savedInstanceState?.getString("PREVIEW_URL")

        val track = intent.getSerializableExtra(SELECTEDTRACK) as? Track
        val trackUrl = track?.previewUrl ?: previewUrl

        if (trackUrl != null) {
            viewModel = ViewModelProvider(
                this, PlayerActivityViewModel.getViewModelFactory(trackUrl)
            )[PlayerActivityViewModel::class.java]
            track?.let { loadTrackInfo(it) }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }

        viewModel.playerState.observe(this, Observer { state ->
            handlePlayerState(state)
        })

        viewModel.currentTime.observe(this, Observer { time ->
            updateTimer(time)
        })

        if (track != null) {
            viewModel.prepare(track)
        }
        binding.play.setOnClickListener {
            val currentState = viewModel.playerState.value
            if (currentState == PlayerState.STATE_PLAYING) {
                viewModel.pause()
            } else {
                viewModel.play()
            }
        }
    }

    private fun loadTrackInfo(track: Track) {
        Log.d("PlayerActivity", "Loading track info: ${track.getCoverArtwork()}")
        Glide.with(this).load(track.getCoverArtwork()).fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one)))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(binding.artworkImageViewBig)
        Log.d("PlayerActivity", "Loading track info: ${track.getCoverArtwork()}")
        binding.trackNameTextView.text = track.trackName
        binding.artworkImageViewBig.isVisible=true
        binding.artistNameTextView.text = track.artistName
        binding.genreTextView.text = track.primaryGenreName ?: noData
        binding.countryTextView.text = track.country ?: noData

        val releaseDateString = track.releaseDate ?: noData
        binding.releaseDateTextView.text = viewModel.parseDate(releaseDateString, noData)
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

    private fun updateTimer(time: Int) {
        binding.timer.isVisible = true
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
        private val timerDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    }
}
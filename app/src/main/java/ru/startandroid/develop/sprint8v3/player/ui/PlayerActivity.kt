package ru.startandroid.develop.sprint8v3.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
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
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.state.PlayerState

const val selectedTrack = "selectedTrack"


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerActivityViewModel
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PlayerActivity", "onCreate called")
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(selectedTrack) as? Track

        if (track != null) {
            viewModel = ViewModelProvider(
                this,
                PlayerActivityViewModel.getViewModelFactory(track.previewUrl.toString())
            )[PlayerActivityViewModel::class.java]
            loadTrackInfo(track)
        }

        Log.d("PlayerActivity", "factorycreated")



        binding.timer.text = "00:00"


        if (track != null) {
            loadTrackInfo(track)
        }

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

    private fun updateTimer(time: Int) {
        binding.timer.visibility = View.VISIBLE

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
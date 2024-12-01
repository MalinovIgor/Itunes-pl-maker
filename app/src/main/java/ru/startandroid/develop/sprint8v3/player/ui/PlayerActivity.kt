package ru.startandroid.develop.sprint8v3.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.ActivityPlayerBinding
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.library.ui.fragment.PlaylistCreationFragment
import ru.startandroid.develop.sprint8v3.player.state.PlayerState
import ru.startandroid.develop.sprint8v3.player.ui.bottomPl.BottomPlAdapter
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.ui.fragment.SearchFragment
import java.text.SimpleDateFormat
import java.util.Locale

const val SELECTEDTRACK = "selectedTrack"

class PlayerActivity : AppCompatActivity() {

    private var bottomSheetState = BottomSheetBehavior.STATE_HIDDEN

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private val track: Track by lazy {
        intent.getSerializableExtra(TRACK_KEY) as? Track
            ?: throw IllegalArgumentException("Track is required")
    }
    private lateinit var viewModel: PlayerActivityViewModel
    var isClickAllowed = true


    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }
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
        setContentView(binding.root)

        val track = intent.getSerializableExtra(SELECTEDTRACK) as? Track
        if (track != null) {
            updateFavoriteState(track.isFavorites)
        }

        viewModel = getViewModel { parametersOf(track?.previewUrl) }
        binding.timer.text = zeroTimer

        val previewUrl = savedInstanceState?.getString("PREVIEW_URL")

        val trackUrl = track?.previewUrl ?: previewUrl

        if (trackUrl != null) {
            track?.let { loadTrackInfo(it) }
        }

        binding.backArrow.setOnClickListener {
            finish()
        }

        viewModel.playerState.observe(this, Observer { state ->
            viewModel.getState()
            handlePlayerState(state)
            handlePlayerState(state)
        })

        viewModel.currentTime.observe(this, Observer { time ->
            updateTimer(time)
        })

        viewModel.observePlayerState().observe(this) { state ->
            viewModel.getState()
        }

        if (track != null) {
            viewModel.prepare()
        }
        binding.play.setOnClickListener {
            val currentState = viewModel.playerState.value
            if (currentState == PlayerState.STATE_PLAYING) {
                viewModel.pause()

            } else {
                viewModel.play()
            }
        }
        viewModel.observeFavoritesState().observe(this) { state ->
            updateFavoriteState(state)
        }

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bottomSheetBehavior.peekHeight = binding.root.height / 3 * 2
                        viewModel.updatePlaylists()
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay?.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay?.visibility = View.VISIBLE
                        viewModel.updatePlaylists()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay?.alpha = (slideOffset + 1f) / 2f
            }
        })

        binding.queue.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.createPlaylistButton.setOnClickListener {
            bottomSheetState = bottomSheetBehavior.state
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            val fragment = PlaylistCreationFragment.newInstance(false)
            Log.d("fromNavContr check","set as ${fragment.fromNavController}")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_view, fragment)
                .addToBackStack(null)
                .commit()
            binding.playerContent?.visibility = View.GONE
            binding.fragmentView?.visibility = View.VISIBLE
        }

        supportFragmentManager.setFragmentResultListener(
            PlaylistCreationFragment.RESULT, this
        ) { _, bundle ->
            val playlistCreated = bundle.getBoolean(PlaylistCreationFragment.RESULT, false)
            if (playlistCreated) {
                bottomSheetBehavior.state = bottomSheetState
                binding.playerContent?.visibility = View.VISIBLE
                binding.fragmentView?.visibility = View.GONE
            }
        }

        binding.overlay?.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val onItemClickListener = BottomPlAdapter.OnItemClickListener { item ->
            onClickDebounce(track!!, item)
        }

        viewModel.observeIsInPlaylist().observe(this) { state ->
            when (state.isInPlaylist) {
                true -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        this,
                        getString(R.string.in_playlist).format(state.playlist.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                false -> {
                    Toast.makeText(
                        this,
                        getString(R.string.not_in_playlist).format(state.playlist.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.recyclePlaylistsView.layoutManager = LinearLayoutManager(this)

        viewModel.observePlaylists().observe(this) { state ->
            binding.recyclePlaylistsView.adapter =
                BottomPlAdapter(state, onItemClickListener)
            binding.recyclePlaylistsView.visibility = View.VISIBLE

        }
    }

    private fun onClickDebounce(
        track: Track,
        playlist: Playlist
    ) {
        if (isClickAllowed) {
            viewModel.onAddToPlaylistClick(track, playlist)
        }
        lifecycleScope.launch {
            delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
    }

    private fun loadTrackInfo(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one)))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(binding.artworkImageViewBig)
        binding.trackNameTextView.text = track.trackName
        binding.artworkImageViewBig.isVisible = true
        binding.artistNameTextView.text = track.artistName
        binding.genreTextView.text = track.primaryGenreName ?: noData
        binding.countryTextView.text = track.country ?: noData
        val releaseDateString = track.releaseDate ?: noData
        binding.releaseDateTextView.text = viewModel.parseDate(releaseDateString, noData)
        binding.collectionNameTextView.text = track.collectionName
        binding.trackTimeTextView.text = timerDateFormat.format(track.trackTime)
        binding.favorite.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }


    }

    private fun updateFavoriteState(state: Boolean) {
        if (state) {
            binding.favorite.setImageResource(R.drawable.fav_added)
        } else {
            binding.favorite.setImageResource(R.drawable.fav)
        }
    }

    private fun handlePlayerState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                binding.play.setImageResource(R.drawable.pause)
            }

            PlayerState.STATE_PAUSED, PlayerState.STATE_STOPPED -> {
                binding.play.setImageResource(R.drawable.play)
            }

            PlayerState.STATE_COMPLETED -> {
                binding.timer.text = zeroTimer
                binding.play.setImageResource(R.drawable.play)
            }

            else -> {
                binding.play.setImageResource(R.drawable.play)
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
        private const val zeroTimer = "00:00"
        const val TRACK_KEY = "TRACK_KEY"
        private val timerDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

        fun newInstance(context: Context, track: Track): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(TRACK_KEY, track)
            }
        }
    }
}
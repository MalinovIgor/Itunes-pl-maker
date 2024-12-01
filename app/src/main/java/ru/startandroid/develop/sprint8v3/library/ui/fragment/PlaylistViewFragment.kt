package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.FragmentPlaylistViewBinding
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistViewViewModel
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivity
import ru.startandroid.develop.sprint8v3.player.ui.SELECTEDTRACK
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.ui.TrackAdapter
import ru.startandroid.develop.sprint8v3.search.utils.debounce
import ru.startandroid.develop.sprint8v3.search.utils.getDefaultImagePath
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewFragment : Fragment() {

    private lateinit var adapter: TrackAdapter
    private val viewModel: PlaylistViewViewModel by viewModel()
    private var _binding: FragmentPlaylistViewBinding? = null
    private val binding get() = _binding!!
    var playlistId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlaylistViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getInt(PLAYLIST_ID_KEY, -1)
        if (playlistId == -1) {
            Toast.makeText(requireContext(), R.string.pl_not_found, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        val onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { track -> openPlayer(track) }

        adapter = TrackAdapter({ item ->
            onTrackClickDebounce(item)
        },     onItemLongClickListener = { item -> onTrackLongClick(item) })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter=adapter
        binding.recyclerView.visibility=View.VISIBLE

        viewModel.loadPlaylist(playlistId)

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            if (playlist == null) {
                Toast.makeText(requireContext(), R.string.pl_not_found, Toast.LENGTH_SHORT).show()
                return@observe
            } else renderUI(playlist)
        }

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1f) / 2f
            }
        })

        val bottomMenuContainer = binding.playlistsBottomMenu
        val bottomMenuBehavior = BottomSheetBehavior.from(bottomMenuContainer)
        bottomMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1f) / 2f
            }
        })

        binding.overlay.setOnClickListener {
            bottomMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.threePointsBtn.post {
            val location = IntArray(2)
            binding.threePointsBtn.getLocationOnScreen(location)
            bottomSheetBehavior.peekHeight =
                binding.root.height - location[1] - binding.threePointsBtn.height
            binding.playlistName.getLocationOnScreen(location)
            bottomMenuBehavior.peekHeight =
                binding.root.height - location[1] - binding.playlistName.height
        }

        binding.threePointsBtn.setOnClickListener {
            bottomMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.shareBtn.setOnClickListener {
            sharePlaylist()
        }
        binding.share.setOnClickListener {
            sharePlaylist()
        }
        binding.deletePlaylist.setOnClickListener {
            deletePlaylist(binding.playlistName.text.toString())
        }


        binding.editPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistViewFragment_to_playlistCreationFragment,
                Bundle().apply {
                    putBoolean(PlaylistCreationFragment.FROM_NAVCONTROLLER_KEY, true)
                    putInt(PlaylistCreationFragment.PLAYLIST_ID_KEY, playlistId)
                })

        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Any>(
            PlaylistCreationFragment.RESULT
        )?.observe(viewLifecycleOwner) { _ ->
            synchronized(this) {
                viewModel.loadPlaylist(playlistId)
            }
        }

        viewModel.observeAllTracks().observe(viewLifecycleOwner) { tracks ->
            if (!tracks.isNullOrEmpty()) {
                binding.listItems.visibility = View.VISIBLE
                binding.nothing.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                val durationInMinutes = (tracks.sumOf { it.trackTime } / 60000).toInt()
                val formattedDuration = String.format("%d", durationInMinutes)
                val minuteWord = getMinutePluralForm(durationInMinutes)
                val trackWord = getPluralForm(tracks.size).format(tracks.size)

                binding.playlistInfo.text = getString(
                    R.string.pl_info,
                    "$formattedDuration $minuteWord",
                    trackWord
                )
                adapter.updateTracks(tracks)

                binding.recyclerView.adapter = adapter
                binding.playlistSmallTracks.text = getPluralForm(tracks.size).format(tracks.size)
            } else {
                binding.listItems.visibility = View.GONE
                binding.nothing.visibility = View.VISIBLE
                binding.playlistInfo.text = getPluralForm(0).format(0)
                binding.playlistSmallTracks.text = getPluralForm(0).format(0)
                adapter.clearTracks()
            }
        }

    }

    private fun deletePlaylist(playlistName: String) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialog
        ).setTitle(getString(R.string.pl_delete))
            .setMessage(getString(R.string.pl_delete_quest).format(playlistName))
            .setNegativeButton(R.string.no) { _, _ ->
            }.setPositiveButton(R.string.yes) { dialog, which ->
                synchronized(this) {
                    viewModel.deletePlaylist()
                    Toast.makeText(requireContext(), "Плейлист $playlistName удалён", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderUI(playlist: Playlist) {
        if (playlist.imagePath.isNullOrEmpty()) {binding.playlistCover.setImageDrawable(
            getDrawable(
                requireContext(), R.drawable.placeholder_image
            )
        )
            binding.playlistCover.marginTop.plus(PLACEHOLDER_MARGIND_dp)
            binding.playlistCover.marginBottom.plus(PLACEHOLDER_MARGIND_dp)
            binding.playlistCover.marginStart.plus(PLACEHOLDER_MARGIND_dp)
            binding.playlistCover.marginEnd.plus(PLACEHOLDER_MARGIND_dp)
            binding.playlistCover.paddingLeft.plus(PLACEHOLDER_MARGIND_dp)
        }
        else {
            binding.playlistCover.setImageURI(
                File(
                    getDefaultImagePath(requireContext()), playlist.imagePath
                ).toUri()
            )
            binding.playlistCover.scaleX = ZOOM
            binding.playlistCover.scaleY = ZOOM

            binding.playlistSmallCover.setImageURI(
                File(
                    getDefaultImagePath(requireContext()), playlist.imagePath
                ).toUri()
            )
        }

        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistSmallName.text = playlist.name
    }

    private fun openPlayer(item: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra(SELECTEDTRACK, item)
        startActivity(intent)
    }

    private fun onTrackLongClick(track: Track) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialog
        ).setTitle(getString(R.string.delete_track_quest))
            .setNeutralButton(R.string.no) { _, _ ->
            }.setPositiveButton(R.string.yes) { dialog, which ->
                synchronized(this) {
                    viewModel.removeTrackFromPlaylist(track.trackId)
                }
            }.show()
    }

    private fun sharePlaylist() {
        if (adapter.getTracks().isEmpty())
            Toast.makeText(requireContext(), R.string.no_track_in_pl, Toast.LENGTH_SHORT)
                .show()
        else {
            var message = "${binding.playlistName.text}\n${binding.playlistDescription.text}\n${
                getPluralForm(adapter.getTracks().size).format(adapter.getTracks().size)
            }\n"
            var i = 1
            adapter.getTracks().forEach { track ->
                message = message + "${i++}. ${track.artistName} - ${track.trackName} (${
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTime)
                })\n"
            }

            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, R.string.app_name)
            }, null)
            startActivity(share)
        }
    }

    private fun getMinutePluralForm(minutes: Int): String {
        val n = minutes % 100
        return when {
            n in 11..14 -> "минут"
            n % 10 == 1 -> "минута"
            n % 10 in 2..4 -> "минуты"
            else -> "минут"
        }
    }

    private fun getPluralForm(num: Int): String {
        val n = num % 100
        return when {
            n in 11..14 -> requireContext().getString(R.string.track_zero)
            n % 10 == 1 -> requireContext().getString(R.string.track)
            n % 10 in 2..4 -> requireContext().getString(R.string.tracks)
            else -> requireContext().getString(R.string.track_zero)
        }
    }



    companion object {
        const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        const val CLICK_DEBOUNCE_DELAY = 250L
        const val PLACEHOLDER_MARGIND_dp = 2000L

        const val ZOOM = 1.1f
        fun newInstance(playlistId: Int) = PlaylistViewFragment().apply {
            arguments = bundleOf(PLAYLIST_ID_KEY to playlistId)
        }
    }

}
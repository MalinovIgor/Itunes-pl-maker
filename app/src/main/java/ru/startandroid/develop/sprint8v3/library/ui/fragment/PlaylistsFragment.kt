package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.FragmentPlaylistsBinding
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel {
        parametersOf()
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createPlaylistButton.setOnClickListener {
            Log.d("testt","gg")
            findNavController().navigate(R.id.action_libraryFragment2_to_playlistCreationFragment)
        }

        binding.playlistsRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), NUMBER_OF_COLUMN_PLAYLISTS)


        playlistsViewModel.observePlaylists().observe(viewLifecycleOwner) { state ->
            binding.playlistsRecyclerview.adapter = PlaylistAdapter(state)
            render(state.size)
        }

        playlistsViewModel.returnPlaylists()
    }

    private fun render(size: Int) {
        if (size > 0) {
            binding.mediaPlaceholderIv.visibility = View.GONE
            binding.libraryPlaceholder.visibility = View.GONE
            binding.playlistsRecyclerview.visibility = View.VISIBLE
        } else {
            binding.mediaPlaceholderIv.visibility = View.VISIBLE
            binding.libraryPlaceholder.visibility = View.VISIBLE
            binding.playlistsRecyclerview.visibility = View.GONE
        }
    }

    companion object {
        private const val NUMBER_OF_COLUMN_PLAYLISTS = 2
        private const val MOCK_KEY = "mockmock"

        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(MOCK_KEY, "1")
            }
        }
    }


}
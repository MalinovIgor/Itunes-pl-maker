package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.startandroid.develop.sprint8v3.databinding.FragmentPlaylistCreationBinding
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel
import ru.startandroid.develop.sprint8v3.library.ui.fragment.PlaylistViewFragment.Companion.PLAYLIST_ID_KEY
import ru.startandroid.develop.sprint8v3.search.utils.getDefaultImagePath
import java.io.File

class PlaylistCreationFragment(val fromNavController: Boolean = true) : Fragment() {

    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri = Uri.EMPTY
    private val viewModel: PlaylistsViewModel by viewModel()
    private var playlistId: Int = -1
    private var _playlist: Playlist? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        binding.btnCreate.isEnabled = false
        binding.backArrow.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.playlistName.doOnTextChanged { s, _, _, _ ->
            binding.btnCreate.isEnabled = s?.isEmpty() != true
        }
        playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)

        if (playlistId > 0) {
            viewModel.getPlaylist(playlistId)
        }

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val divider = requireActivity().findViewById<View>(R.id.divider)
        if (bottomNavigationView != null && divider != null) {
            bottomNavigationView.visibility = View.GONE
            divider.visibility = View.GONE
        }

        binding.btnCreate.setOnClickListener {
            val playlistName = binding.playlistName.text.toString()
            if (playlistId < 1) {
                viewModel.createPlaylist(
                    playlistName,
                    binding.playlistDescription.text.toString(),
                    imageUri,
                    binding.image.drawable.toBitmap()
                )
                Toast.makeText(
                    context,
                    context?.getString(R.string.pl_created)?.format(playlistName),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.updatePlaylist(
                    playlistName,
                    binding.playlistDescription.text.toString(),
                    binding.image.drawable.toBitmap()
                )
            }

            closeFragment()
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->
            binding.newPlText.text = getString(R.string.edit)
            binding.btnCreate.text = getString(R.string.save)
            if (playlist != null) {
                _playlist = playlist
                if (playlist.imagePath.isNullOrEmpty()) {
                    binding.image.setImageDrawable(
                        getDrawable(requireContext(), R.drawable.placeholder_image)
                    )
                    binding.image.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    binding.image.setImageURI(
                        File(
                            getDefaultImagePath(requireContext()), playlist.imagePath
                        ).toUri()
                    )
                }
                binding.playlistName.setText(playlist.name)
                binding.playlistDescription.setText((playlist.description))
            }
        }



        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.image.scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(this)
                        .load(uri)
                        .apply(
                            RequestOptions().transform(
                                MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(
                                        resources.getDimensionPixelSize(R.dimen.small_one)
                                    )
                                )
                            )
                        )
                        .into(binding.image)
                    imageUri = uri
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.playlistName.text.toString().isNotEmpty() && playlistId < 1) {
                MaterialAlertDialogBuilder(context!!).setTitle(R.string.exit_quest_end)
                    .setMessage(R.string.no_save_exit)
                    .setNeutralButton(android.R.string.cancel) { dialog, which ->

                    }.setPositiveButton(R.string.finish) { dialog, which ->
                        closeFragment()
                    }.show()
            } else closeFragment()
        }
    }

    private fun closeFragment() {
        val result = Bundle()
        if (fromNavController){
            findNavController().previousBackStackEntry?.savedStateHandle?.set(RESULT, result)
        findNavController().navigateUp()}
        else{
            parentFragmentManager.setFragmentResult(RESULT, result)

        parentFragmentManager.popBackStack()}
    }

    companion object {
        const val RESULT = "RESULT_KEY"
        const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"
        const val FROM_NAVCONTROLLER_KEY = "FROM_NAVCONTROLLER_KEY"
        fun newInstance(fromNavController: Boolean, playlistId: Int = -1) =
            PlaylistCreationFragment().apply {
                arguments = bundleOf(
                    FROM_NAVCONTROLLER_KEY to fromNavController,
                    PLAYLIST_ID_KEY to playlistId
                )
            }
    }
}


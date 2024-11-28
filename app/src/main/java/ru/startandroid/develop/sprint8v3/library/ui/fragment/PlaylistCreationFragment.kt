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
import androidx.core.graphics.drawable.toBitmap
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
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel

class PlaylistCreationFragment(val fromNavController: Boolean = true) : Fragment() {

    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri = Uri.EMPTY
    private val viewModel: PlaylistsViewModel by viewModel()

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

        binding.btnCreate.setOnClickListener {
            val playlistName = binding.playlistName.text.toString()
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

            closeFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.playlistName.text.toString().isNotEmpty()) {
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
        if (fromNavController)
            findNavController().previousBackStackEntry?.savedStateHandle?.set(RESULT, result)
        else
            parentFragmentManager.setFragmentResult(RESULT, result)

        parentFragmentManager.popBackStack()
    }

    companion object {
        const val RESULT = "RESULT_KEY"
        fun newInstance(fromNavController: Boolean) = PlaylistCreationFragment(fromNavController)
    }
}


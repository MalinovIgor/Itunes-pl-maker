package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.startandroid.develop.sprint8v3.databinding.FragmentPlaylistCreationBinding
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel

class PlaylistCreationFragment(fromNavController: Boolean) : Fragment() {
    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri = Uri.EMPTY
    private val viewModel: PlaylistsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            context?.let {
                if (binding.playlistName.text?.isNotEmpty() == true) {
                    MaterialAlertDialogBuilder(it)
                        .setTitle("Завершить создание плейлиста?")
                        .setMessage("Все несохраненные данные будут потеряны")
                        .setNeutralButton("Отмена") { dialog, which ->
                        }

                        .setPositiveButton("Завершить") { dialog, which ->
                            findNavController().popBackStack()
                        }
                        .show()
                } else
                    findNavController().popBackStack()
            }
        }
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistName.doOnTextChanged { s, _, _, _ ->
            binding.btnCreate.isEnabled = s?.isEmpty() != true
        }

        binding.backArrow.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
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
                                    RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one))
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
            viewLifecycleOwner.lifecycleScope.launch {
                val playlistName = binding.playlistName.text.toString()
                viewModel.createPlaylist(
                    playlistName,
                    binding.playlistDescription.text.toString(),
                    imageUri,
                    binding.image.drawable.toBitmap()
                )
                Toast.makeText(requireContext(),"$imageUri",Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(
                requireContext(),
                "Плейлист ${binding.playlistName.text.toString()} создан",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }


    companion object {
        const val RESULT = "RESULT_KEY"
        fun newInstance(fromNavController: Boolean) = PlaylistCreationFragment(fromNavController)
    }
}
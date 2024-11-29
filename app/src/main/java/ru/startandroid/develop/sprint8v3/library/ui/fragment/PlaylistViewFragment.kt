package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.FragmentPlaylistsBinding
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistViewViewModel

class PlaylistViewFragment: Fragment() {

    private val viewModel: PlaylistViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_playlist_view, container, false)
    }

    companion object {
        fun newInstance() = PlaylistViewFragment()
    }
}
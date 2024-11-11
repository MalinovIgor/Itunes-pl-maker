package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.startandroid.develop.sprint8v3.databinding.FragmentFavoritesBinding
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivity
import ru.startandroid.develop.sprint8v3.player.ui.SELECTEDTRACK
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.ui.TrackAdapter
import ru.startandroid.develop.sprint8v3.search.ui.fragment.SearchFragment

class FavoritesFragment : Fragment(), TrackAdapter.Listener
{

    private val favoritesViewModel: FavoritesViewModel by viewModel {
        parametersOf()
    }

    private var binding: FragmentFavoritesBinding? = null
    private lateinit var adapter: TrackAdapter
    var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        adapter = TrackAdapter(this, ArrayList())

        binding?.recyclerView?.adapter = adapter
        favoritesViewModel.returnFavoriteTracks()
        favoritesViewModel.getFavoriteTracks().observe(viewLifecycleOwner) { favs ->
            if (favs.isEmpty()) {
                binding?.placeholderImage?.isVisible = true
                binding?.placeholderText?.isVisible = true
                binding?.recyclerView?.isVisible = false
            } else {
                updateFavoriteState(favs)
                binding?.placeholderImage?.isVisible = false
                binding?.placeholderText?.isVisible = false
                binding?.recyclerView?.isVisible = true
            }
        }
    }

    private fun updateFavoriteState(data: List<Track>) {
        adapter = TrackAdapter(this, ArrayList(data))
        binding?.recyclerView?.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.returnFavoriteTracks()
    }



    override fun onClick(track: Track) {
        if (isClickAllowed) {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(SELECTEDTRACK, track)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
    }

    companion object {
        private const val MOCK_KEY = "mockmock"

        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle().apply {
                putString(MOCK_KEY, "1")
            }
        }
    }
}


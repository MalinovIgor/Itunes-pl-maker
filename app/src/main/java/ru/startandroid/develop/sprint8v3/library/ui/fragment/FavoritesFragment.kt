package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.startandroid.develop.sprint8v3.databinding.FragmentFavoritesBinding
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel

class FavoritesFragment : Fragment()
{

    private val favoritesViewModel: FavoritesViewModel by viewModel {
        parametersOf()
    }

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        private const val MOCK_KEY = "poster_url"

        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle().apply {
                putString(MOCK_KEY, "1")
            }
        }
    }

}
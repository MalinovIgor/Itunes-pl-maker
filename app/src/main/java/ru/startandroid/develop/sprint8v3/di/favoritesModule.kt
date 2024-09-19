package ru.startandroid.develop.sprint8v3.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel

val favoritesModule = module {

    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavoritesViewModel()
    }


}
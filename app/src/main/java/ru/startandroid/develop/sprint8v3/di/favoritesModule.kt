package ru.startandroid.develop.sprint8v3.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel
import ru.startandroid.develop.sprint8v3.library.db.AppDatabase

val favoritesModule = module {

    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavoritesViewModel()
    }

}
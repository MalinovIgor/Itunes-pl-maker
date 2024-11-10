package ru.startandroid.develop.sprint8v3.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.data.FavoritesRepositoryImpl
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.track.TrackDbConvertor
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.library.domain.impl.FavoritesInteractorImpl

val favoritesModule = module {

    viewModel {
        PlaylistsViewModel()
    }
    viewModel {
        FavoritesViewModel(get())
    }

    single<FavoritesRepository>{
        FavoritesRepositoryImpl(get(),get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single {
        TrackDbConvertor()
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
package ru.startandroid.develop.sprint8v3.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.data.FavoritesRepositoryImpl
import ru.startandroid.develop.sprint8v3.library.data.PlaylistRepositoryImpl
import ru.startandroid.develop.sprint8v3.library.ui.FavoritesViewModel
import ru.startandroid.develop.sprint8v3.library.ui.PlaylistsViewModel
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.track.TrackDbConvertor
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.library.domain.db.PlaylistRepository
import ru.startandroid.develop.sprint8v3.library.domain.impl.FavoritesInteractorImpl
import ru.startandroid.develop.sprint8v3.library.domain.impl.PlaylistInteractorImpl

val favoritesModule = module {

    viewModel {
        PlaylistsViewModel(get(),get())
    }
    viewModel {
        FavoritesViewModel(get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single {
        TrackDbConvertor()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

}
package ru.startandroid.develop.sprint8v3.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.data.FavoritesRepositoryImpl
import ru.startandroid.develop.sprint8v3.library.db.AppDatabase
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.library.domain.impl.FavoritesInteractorImpl
import ru.startandroid.develop.sprint8v3.search.data.repository.SearchHistoryRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.domain.repository.SearchHistoryRepository

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

        single<FavoritesInteractor> {
            FavoritesInteractorImpl(get())
        }

}
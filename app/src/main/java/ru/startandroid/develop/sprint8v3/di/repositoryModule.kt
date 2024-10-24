package ru.startandroid.develop.sprint8v3.di

import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.data.FavoritesRepositoryImpl
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository

val repositoryModule = module {

    single <FavoritesRepository> {
        FavoritesRepositoryImpl(get(),get())
    }

}
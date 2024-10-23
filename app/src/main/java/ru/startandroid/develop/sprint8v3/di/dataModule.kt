package ru.startandroid.develop.sprint8v3.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.library.ui.db.AppDatabase

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
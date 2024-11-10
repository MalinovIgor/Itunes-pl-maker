package ru.startandroid.develop.sprint8v3.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.startandroid.develop.sprint8v3.library.data.InFavoritesCheckRepository
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.impl.FavoritesInteractorImpl
import ru.startandroid.develop.sprint8v3.search.utils.SEARCH_HISTORY_KEY
import ru.startandroid.develop.sprint8v3.search.data.network.ItunesAPI
import ru.startandroid.develop.sprint8v3.search.data.network.RetrofitNetworkClient
import ru.startandroid.develop.sprint8v3.search.data.repository.SearchHistoryRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.data.repository.TracksRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.domain.NetworkClient
import ru.startandroid.develop.sprint8v3.search.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.search.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.search.domain.impl.HistoryInteractorImpl
import ru.startandroid.develop.sprint8v3.search.domain.impl.TracksInteractorImpl
import ru.startandroid.develop.sprint8v3.search.domain.repository.SearchHistoryRepository
import ru.startandroid.develop.sprint8v3.search.domain.repository.TracksRepository
import ru.startandroid.develop.sprint8v3.search.ui.SearchActivityViewModel

val searchModule = module {

    single {
        androidContext().getSharedPreferences(SEARCH_HISTORY_KEY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<ItunesAPI> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ItunesAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(),get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(),get())
    }

    single { InFavoritesCheckRepository(get()) }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    viewModel {
        SearchActivityViewModel(
            get<Application>(),
            get<TracksInteractor>(),
            get<HistoryInteractor>()
        )
    }
}
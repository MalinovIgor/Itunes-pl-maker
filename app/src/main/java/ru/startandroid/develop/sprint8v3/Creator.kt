package ru.startandroid.develop.sprint8v3

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import ru.startandroid.develop.sprint8v3.search.data.network.RetrofitNetworkClient
import ru.startandroid.develop.sprint8v3.search.data.repository.SearchHistoryRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.data.repository.ThemeSettingsRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.data.repository.TracksRepositoryImpl
import ru.startandroid.develop.sprint8v3.search.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.search.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.search.domain.impl.HistoryInteractorImpl
import ru.startandroid.develop.sprint8v3.search.domain.impl.TracksInteractorImpl
import ru.startandroid.develop.sprint8v3.search.domain.repository.SearchHistoryRepository
import ru.startandroid.develop.sprint8v3.search.domain.repository.TracksRepository
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.domain.impl.PlayerInteractorImpl
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.impl.ThemeSettingsInteractorImpl
import ru.startandroid.develop.sprint8v3.settings.domain.repository.ThemeSettingsRepository
import ru.startandroid.develop.sprint8v3.ui.Settings.USER_PREFERENCES

object Creator {
    private lateinit var application: Application
    private const val HISTORY_NAME = "history_name"



    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSettingsRepository() : ThemeSettingsRepository {
        return ThemeSettingsRepositoryImpl(application.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE))
    }

    fun provideSettingsInteractor(): ThemeSettingsInteractor {
        return ThemeSettingsInteractorImpl(provideSettingsRepository())
    }

    fun provideSharedPreferences(key: String): SharedPreferences {
        return application.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    fun provideHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideSharedPreferences(HISTORY_NAME))
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(provideHistoryRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(provideMediaPlayer(),
            )
    }
    private fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

}
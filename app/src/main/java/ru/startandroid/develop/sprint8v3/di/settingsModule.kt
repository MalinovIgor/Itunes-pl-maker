package ru.startandroid.develop.sprint8v3.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.search.data.repository.ThemeSettingsRepositoryImpl
import ru.startandroid.develop.sprint8v3.settings.domain.api.ShareRepository
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.impl.ShareRepositoryImpl
import ru.startandroid.develop.sprint8v3.settings.domain.impl.ThemeSettingsInteractorImpl
import ru.startandroid.develop.sprint8v3.settings.domain.repository.ThemeSettingsRepository
import ru.startandroid.develop.sprint8v3.settings.ui.ThemeSettingsActivityViewModel
import ru.startandroid.develop.sprint8v3.ui.Settings.USER_PREFERENCES

val settingsModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            USER_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    factory<ThemeSettingsInteractor> { ThemeSettingsInteractorImpl(get(), get()) }


    factory<ThemeSettingsRepository> { ThemeSettingsRepositoryImpl(get()) }
    factory<ShareRepository> { ShareRepositoryImpl(get()) }

    viewModel {
        ThemeSettingsActivityViewModel(get())
    }
}


package ru.startandroid.develop.sprint8v3.ui.Settings

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.startandroid.develop.sprint8v3.di.playerModule
import ru.startandroid.develop.sprint8v3.di.searchModule
import ru.startandroid.develop.sprint8v3.di.settingsModule
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor

const val DARK_THEME = "dark_theme"
const val USER_PREFERENCES = "user_preferences"
var darkTheme: Boolean = false

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                searchModule,
                playerModule,
                settingsModule,
            )
        }

        val mainThemeInt: ThemeSettingsInteractor by inject()

        switchTheme(mainThemeInt.isThemeNight())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPreferences: SharedPreferences by inject()
        sharedPreferences.edit { putBoolean(DARK_THEME, darkTheme) }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
}
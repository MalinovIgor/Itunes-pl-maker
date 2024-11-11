package ru.startandroid.develop.sprint8v3

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.room.Room
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.startandroid.develop.sprint8v3.di.favoritesModule
import ru.startandroid.develop.sprint8v3.di.playerModule
import ru.startandroid.develop.sprint8v3.di.repositoryModule
import ru.startandroid.develop.sprint8v3.di.searchModule
import ru.startandroid.develop.sprint8v3.di.settingsModule
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase

const val DARK_THEME = "dark_theme"
const val USER_PREFERENCES = "user_preferences"
var darkTheme: Boolean = false

class App : Application() {
    lateinit var database: AppDatabase
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                searchModule,
                playerModule,
                settingsModule,
                favoritesModule,
                repositoryModule
            )
        }
        val sharedPreferences: SharedPreferences by inject()
        if (!sharedPreferences.contains(DARK_THEME)) {
            val systemTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isSystemDark = systemTheme == Configuration.UI_MODE_NIGHT_YES
            switchTheme(isSystemDark)
        } else {
            switchTheme(sharedPreferences.getBoolean(DARK_THEME, false))
        }
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
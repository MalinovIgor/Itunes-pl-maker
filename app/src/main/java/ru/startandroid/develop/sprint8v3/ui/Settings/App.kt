package ru.startandroid.develop.sprint8v3.ui.Settings

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.Creator.provideSharedPreferences
import ru.startandroid.develop.sprint8v3.di.playerModule
import ru.startandroid.develop.sprint8v3.di.searchModule
import ru.startandroid.develop.sprint8v3.di.settingsModule
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor

const val DARK_THEME = "dark_theme"
const val USER_PREFERENCES = "user_preferences"
var darkTheme:Boolean=false
private lateinit var sharedPreferences: SharedPreferences

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                searchModule,
                playerModule,
             //   settingsModule,
            )
        }

//        val mainThemeInt: ThemeSettingsInteractor by inject()

//        switchTheme(mainThemeInt.isThemeNight())
//        Log.e("KoinNotStarted", "00")

        Creator.initApplication(this)
        sharedPreferences = provideSharedPreferences(USER_PREFERENCES)


        val settingsInteractor = Creator.provideSettingsInteractor()
        val isDarkTheme = settingsInteractor.isThemeNight()
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    fun switchTheme(darkThemeEnabled:Boolean){
        darkTheme = darkThemeEnabled
        sharedPreferences.edit{ putBoolean(DARK_THEME, darkTheme)}
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
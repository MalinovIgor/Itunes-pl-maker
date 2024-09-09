package ru.startandroid.develop.sprint8v3.ui.Settings

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.Creator.provideSharedPreferences

const val DARK_THEME = "dark_theme"
const val USER_PREFERENCES = "user_preferences"
var darkTheme:Boolean=false
private lateinit var sharedPreferences: SharedPreferences

class App : Application() {
    override fun onCreate() {
        super.onCreate()
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
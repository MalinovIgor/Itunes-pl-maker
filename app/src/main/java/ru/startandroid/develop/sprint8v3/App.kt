package ru.startandroid.develop.sprint8v3

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val darkTheme = "dark_theme"
const val userPreferences = "user_preferences"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(userPreferences, MODE_PRIVATE)
        val isDarkTheme = sharedPrefs.getBoolean(darkTheme, false)

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val sharedPrefs = getSharedPreferences(userPreferences, MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(darkTheme, darkThemeEnabled)
        editor.apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
package ru.startandroid.develop.sprint8v3.search.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import ru.startandroid.develop.sprint8v3.settings.domain.repository.ThemeSettingsRepository
import ru.startandroid.develop.sprint8v3.ui.Settings.DARK_THEME

class ThemeSettingsRepositoryImpl (private val sharedPreferences: SharedPreferences) :
    ThemeSettingsRepository {
    override fun isThemeNight(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    override fun setThemeNight(darkThemeEnabled: Boolean) {
        sharedPreferences.edit{putBoolean(DARK_THEME, darkThemeEnabled)}
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })
    }
}
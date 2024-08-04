package ru.startandroid.develop.sprint8v3.domain.api

import android.content.SharedPreferences

interface SettingsInteractor {
    fun getThemePreference() : Boolean

    fun setThemePreference(darkThemeEnabled:Boolean)
}
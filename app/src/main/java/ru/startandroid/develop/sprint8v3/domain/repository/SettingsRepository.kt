package ru.startandroid.develop.sprint8v3.domain.repository

import android.content.SharedPreferences

interface SettingsRepository {

    fun getThemePreference() : Boolean

    fun setThemePreference(darkThemeEnabled:Boolean)
}
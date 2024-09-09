package ru.startandroid.develop.sprint8v3.settings.domain.repository

interface ThemeSettingsRepository {

    fun isThemeNight() : Boolean

    fun setThemeNight(darkThemeEnabled:Boolean)
}
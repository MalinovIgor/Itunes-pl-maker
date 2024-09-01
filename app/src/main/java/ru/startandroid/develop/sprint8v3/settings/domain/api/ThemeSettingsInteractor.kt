package ru.startandroid.develop.sprint8v3.settings.domain.api

interface ThemeSettingsInteractor {
    fun isThemeNight() : Boolean

    fun setThemeNight(darkThemeEnabled:Boolean)
}
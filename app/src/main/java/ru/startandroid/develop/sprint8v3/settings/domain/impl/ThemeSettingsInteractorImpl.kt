package ru.startandroid.develop.sprint8v3.settings.domain.impl

import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.repository.ThemeSettingsRepository

class ThemeSettingsInteractorImpl (private val repository: ThemeSettingsRepository) : ThemeSettingsInteractor {
    override fun isThemeNight(): Boolean {
        return repository.isThemeNight()
    }

    override fun setThemeNight(darkThemeEnabled: Boolean) {
        repository.setThemeNight(darkThemeEnabled)
    }

}
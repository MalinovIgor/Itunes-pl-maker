package ru.startandroid.develop.sprint8v3.settings.domain.repository

import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData

interface ThemeSettingsRepository {

    fun isThemeNight() : Boolean
    fun setThemeNight(darkThemeEnabled:Boolean)


}
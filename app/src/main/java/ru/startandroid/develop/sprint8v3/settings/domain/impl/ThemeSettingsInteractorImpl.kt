package ru.startandroid.develop.sprint8v3.settings.domain.impl

import ru.startandroid.develop.sprint8v3.settings.domain.api.ShareRepository
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData
import ru.startandroid.develop.sprint8v3.settings.domain.repository.ThemeSettingsRepository

class ThemeSettingsInteractorImpl(
    private val repository: ThemeSettingsRepository,
    private val shareRepository: ShareRepository
) : ThemeSettingsInteractor {
    override fun isThemeNight(): Boolean {
        return repository.isThemeNight()
    }

    override fun setThemeNight(darkThemeEnabled: Boolean) {
        return repository.setThemeNight(darkThemeEnabled)
    }

    override fun getShareData(): ShareData {
        return shareRepository.getShareData()
    }

    override fun getMailData(): MailData {
        return shareRepository.getMailData()
    }

    override fun getAgreementData(): AgreementData {
        return shareRepository.getAgreementData()
    }

}
package ru.startandroid.develop.sprint8v3.settings.domain.api

import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData

interface ShareRepository {
    fun getShareData(): ShareData
    fun getMailData(): MailData
    fun getAgreementData(): AgreementData
}
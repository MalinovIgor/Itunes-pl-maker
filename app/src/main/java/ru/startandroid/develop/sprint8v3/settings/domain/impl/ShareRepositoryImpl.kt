package ru.startandroid.develop.sprint8v3.settings.domain.impl

import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.settings.domain.api.ShareRepository
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData
import android.content.Context

class ShareRepositoryImpl(private val context: Context) : ShareRepository {


    override fun getShareData(): ShareData {
        return ShareData(
            url = context.getString(R.string.shareAndroidDevLink),
        )
    }

    override fun getMailData(): MailData {
        return MailData(
            mail = context.getString(R.string.myEmail),
            subject = context.getString(R.string.mailSubject),
            text = context.getString(R.string.mailText)
        )
    }

    override fun getAgreementData(): AgreementData {
        return AgreementData(
            link = context.getString(R.string.agreementLink),
        )
    }
}
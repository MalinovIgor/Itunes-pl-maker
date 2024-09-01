package ru.startandroid.develop.sprint8v3.settings.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.SingleLiveEvent
import ru.startandroid.develop.sprint8v3.settings.domain.api.ShareRepository
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData

class ThemeSettingsActivityViewModel (private val application: Application,
                                      private val themeInteractor: ThemeSettingsInteractor
): ViewModel(), ShareRepository {

    private val termsState = SingleLiveEvent<AgreementData>()
    private val shareState = SingleLiveEvent<ShareData>()
    private val supportState = SingleLiveEvent<MailData>()

    init {
        termsState.postValue(getAgreementData())
        shareState.postValue(getShareData())
        supportState.postValue(getMailData())
    }

    fun observeTermsState(): LiveData<AgreementData> = termsState
    fun observeShareState(): LiveData<ShareData> = shareState
    fun observeSupportState(): LiveData<MailData> = supportState

    private val isNightThemeEnabled = MutableLiveData(themeInteractor.isThemeNight())

    fun updateThemeState(isThemeNight: Boolean) {
        themeInteractor.setThemeNight(isThemeNight)
        isNightThemeEnabled.postValue(isThemeNight)
    }

    fun observeThemeState(): LiveData<Boolean> = isNightThemeEnabled

    override fun getShareData(): ShareData {
        return ShareData(
            url = application.getString(R.string.shareAndroidDevLink),
        )
    }

    override fun getMailData(): MailData {
        return MailData(
            mail = application.getString(R.string.myEmail),
            subject = application.getString(R.string.mailSubject),
            text = application.getString(R.string.mailText)
        )
    }

    override fun getAgreementData(): AgreementData {
        return AgreementData(
            link = application.getString(R.string.agreementLink),
        )
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ThemeSettingsActivityViewModel(
                    this[APPLICATION_KEY] as Application,
                    Creator.provideSettingsInteractor()
                )
            }
        }
    }
}
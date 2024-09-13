package ru.startandroid.develop.sprint8v3.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.startandroid.develop.sprint8v3.SingleLiveEvent
import ru.startandroid.develop.sprint8v3.settings.domain.api.ThemeSettingsInteractor
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData

class ThemeSettingsActivityViewModel(
    private val themeInteractor: ThemeSettingsInteractor
) : ViewModel() {

    private val termsState = SingleLiveEvent<AgreementData>()
    private val shareState = SingleLiveEvent<ShareData>()
    private val supportState = SingleLiveEvent<MailData>()

    init {
        termsState.postValue(themeInteractor.getAgreementData())
        shareState.postValue(themeInteractor.getShareData())
        supportState.postValue(themeInteractor.getMailData())
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
}

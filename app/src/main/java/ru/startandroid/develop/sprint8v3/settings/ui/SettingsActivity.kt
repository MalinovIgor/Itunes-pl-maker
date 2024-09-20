package ru.startandroid.develop.sprint8v3.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.ActivitySettingsBinding
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData
import ru.startandroid.develop.sprint8v3.App
import ru.startandroid.develop.sprint8v3.ui.main.MainActivity

class SettingsActivity : AppCompatActivity() {


    private val binding: ActivitySettingsBinding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<ThemeSettingsActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.nightThemeSwitch.isChecked = viewModel.observeThemeState().value!!
        binding.nightThemeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            viewModel.updateThemeState(checked)
        }
        binding.backArrow.setOnClickListener {
            val displayIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            displayIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(displayIntent)
        }
        binding.share.setOnClickListener {
            viewModel.observeShareState().observe(this) { sData ->
                shareCourseLink(sData)
            }
        }

        binding.agreementView.setOnClickListener {
            viewModel.observeTermsState().observe(this) { tData ->
                openAgreement(tData)
            }
        }

        binding.sendToSupport.setOnClickListener {
            viewModel.observeSupportState().observe(this) { mData ->
                sendEmail(mData)
            }
        }

    }


    private fun shareCourseLink(data: ShareData) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data.url)
            setType("text/plain")
        }, null)
        startActivity(share)
    }


    private fun sendEmail(data: MailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        val mailtoPrefix = getString(R.string.mailto_prefix)
        emailIntent.data = Uri.parse(mailtoPrefix)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, data.mail)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, data.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, data.text)
        startActivity(emailIntent)
    }

    private fun openAgreement(data: AgreementData) {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(data.link)

        startActivity(agreementIntent)
    }
}

package ru.startandroid.develop.sprint8v3.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.startandroid.develop.sprint8v3.App
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.databinding.FragmentSettingsBinding
import ru.startandroid.develop.sprint8v3.settings.domain.model.AgreementData
import ru.startandroid.develop.sprint8v3.settings.domain.model.MailData
import ru.startandroid.develop.sprint8v3.settings.domain.model.ShareData
import ru.startandroid.develop.sprint8v3.ui.main.MainActivity

class SettingsFragment : Fragment(){
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<ThemeSettingsActivityViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nightThemeSwitch.isChecked = viewModel.observeThemeState().value!!
        binding.nightThemeSwitch.setOnCheckedChangeListener { _, checked ->
            (viewLifecycleOwner as App).switchTheme(checked)
            viewModel.updateThemeState(checked)
        }
        binding.backArrow.setOnClickListener {
            val displayIntent = Intent(requireContext(), MainActivity::class.java)
            displayIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(displayIntent)
        }
        binding.share.setOnClickListener {
            viewModel.observeShareState().observe(viewLifecycleOwner) { sData ->
                shareCourseLink(sData)
            }
        }

        binding.agreementView.setOnClickListener {
            viewModel.observeTermsState().observe(viewLifecycleOwner) { tData ->
                openAgreement(tData)
            }
        }

        binding.sendToSupport.setOnClickListener {
            viewModel.observeSupportState().observe(viewLifecycleOwner) { mData ->
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

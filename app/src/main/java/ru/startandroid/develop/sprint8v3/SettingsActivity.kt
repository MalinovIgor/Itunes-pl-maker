package ru.startandroid.develop.sprint8v3

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    private lateinit var switchTheme: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_arrow)

        backButton.setOnClickListener {
            val displayIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            displayIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(displayIntent)
        }


        val shareButton = findViewById<RelativeLayout>(R.id.share)
        shareButton.setOnClickListener {
            shareCourseLink()
        }


        val sendToSupport = findViewById<RelativeLayout>(R.id.sendToSupport)
        sendToSupport.setOnClickListener {
            val mailSubject = getString(R.string.mailSubject)
            val mailText = getString(R.string.mailText)
            val mailReceiver = getString(R.string.myEmail)
            sendEmail(mailReceiver, mailSubject, mailText)
        }

        val agreementView = findViewById<RelativeLayout>(R.id.agreementView)
        val agreementLink = getString(R.string.agreementLink)
        agreementView.setOnClickListener {
            openAgreement(agreementLink)
        }

        switchTheme = findViewById(R.id.nightThemeSwitch)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        switchTheme.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun shareCourseLink() {
        val shareAndroidDevLink = getString(R.string.shareAndroidDevLink)
        val shareIntent = Intent(Intent.ACTION_SEND)
        val shareType = getString(R.string.share_intent_type)
        shareIntent.type = shareType
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareAndroidDevLink)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    }

    private fun sendEmail(mailReceiver: String, mailSubject: String, mailText: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        val mailtoPrefix = getString(R.string.mailto_prefix)
        emailIntent.data = Uri.parse(mailtoPrefix)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailReceiver))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, mailText)

        startActivity(emailIntent)
    }

    private fun openAgreement(link: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(link)

        startActivity(agreementIntent)
    }

}

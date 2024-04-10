package ru.startandroid.develop.sprint8v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_arrow)

        backButton.setOnClickListener {
            val displayIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            displayIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(displayIntent)
        }

    }
}
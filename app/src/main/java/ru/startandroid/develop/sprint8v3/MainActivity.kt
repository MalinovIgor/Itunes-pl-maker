package ru.startandroid.develop.sprint8v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<LinearLayout>(R.id.search_button)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displayIntent)
            }
        }

        searchButton.setOnClickListener(buttonClickListener)

        val mediatekaButton = findViewById<LinearLayout>(R.id.mediateka)

        mediatekaButton.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, MediatekaActivity::class.java)
            startActivity(displayIntent)
        }

        val settingsButton = findViewById<LinearLayout>(R.id.settings)

        settingsButton.setOnClickListener {
            val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displayIntent)
        }


    }
}
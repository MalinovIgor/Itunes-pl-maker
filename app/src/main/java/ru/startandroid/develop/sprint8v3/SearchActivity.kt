package ru.startandroid.develop.sprint8v3

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backFromSearch = findViewById<ImageView>(R.id.back_from_search)
        backFromSearch.setOnClickListener {
            val displayIntent = Intent(this@SearchActivity, MainActivity::class.java)
            displayIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(displayIntent)
        }

        val editTextToBeCleared = findViewById<EditText>(R.id.edit_text)
        val clearEditText = findViewById<ImageView>(R.id.clear_text)

        clearEditText.setOnClickListener {
            editTextToBeCleared.setText("")
        }

        clearEditText.visibility = View.INVISIBLE

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearEditText.visibility = View.INVISIBLE
                } else {
                    clearEditText.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editTextToBeCleared.addTextChangedListener(textWatcher)



    }
}
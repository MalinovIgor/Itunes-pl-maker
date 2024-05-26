package ru.startandroid.develop.sprint8v3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private val itunesBaseURL = "https://itunes.apple.com"

    private val retrofit2 =
        Retrofit.Builder().baseUrl(itunesBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val itunesService = retrofit2.create(ItunesAPI::class.java)
    private val tracks = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderErrorImage :ImageView
    private lateinit var buttonUpdate : LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        dataFromTextEdit = savedInstanceState?.getString(dataFromTextEditKey) ?: ""

        val backFromSearch = findViewById<ImageView>(R.id.back_from_search)
        backFromSearch.setOnClickListener {
            finish()
        }

        editText = findViewById<EditText>(R.id.edit_text)
        val clearEditText = findViewById<ImageView>(R.id.clear_text)

        clearEditText.setOnClickListener {
            editText.setText("")
            dataFromTextEdit = ""
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearEditText.visibility = View.INVISIBLE
                } else {
                    clearEditText.visibility = View.VISIBLE
                    dataFromTextEdit = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        editText.addTextChangedListener(textWatcher)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TrackAdapter()
        recyclerView.adapter = adapter

        placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)
        buttonUpdate = findViewById(R.id.update)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(this, "searchstart!", Toast.LENGTH_SHORT).show()
                search()
                true
            } else {
                Toast.makeText(this, "Action ID: $actionId", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(dataFromTextEditKey, dataFromTextEdit)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (::editText.isInitialized) {
            dataFromTextEdit = savedInstanceState.getString(dataFromTextEditKey) ?: ""
            editText.setText(dataFromTextEdit)
        }
    }

    private fun showMessage(message: String, additionalMessage: String) {
        if (message.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            tracks.clear()
            adapter.updateTracks(tracks)
            placeholderMessage.text = message
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
            placeholderErrorImage.visibility = View.GONE
            buttonUpdate.visibility = View.GONE
        }
    }

    private fun search() {
        Toast.makeText(this, "Executing search", Toast.LENGTH_SHORT).show()
        val query = editText.text.toString()
        Toast.makeText(this, "Query: $query", Toast.LENGTH_SHORT).show()
        placeholderErrorImage = findViewById<ImageView>(R.id.placeholderErrorImage)


        itunesService.search(query)
            .enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    Toast.makeText(
                        this@SearchActivity,
                        "Search response received",
                        Toast.LENGTH_SHORT
                    ).show()
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                adapter.updateTracks(tracks)
                                Toast.makeText(
                                    this@SearchActivity,
                                    "Tracks updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (tracks.isEmpty()) {
                                showMessage(getString(R.string.nothing_found), "")
                                placeholderErrorImage.visibility = View.VISIBLE
                                placeholderMessage.setText(R.string.nothing_found)
                                placeholderErrorImage.setImageResource(R.drawable.nothings_found)
                            } else {
                                showMessage("", "")
                            }
                        }

                        else -> {
                            showMessage(
                                getString(R.string.connection_trouble),
                                response.code().toString()
                            )
                            placeholderErrorImage.visibility = View.VISIBLE
                            placeholderMessage.setText(R.string.connection_trouble)
                            placeholderErrorImage.setImageResource(R.drawable.connecton_trouble)
                            buttonUpdate.visibility = View.VISIBLE

                        }
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    Toast.makeText(
                        this@SearchActivity,
                        "Search failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    showMessage(getString(R.string.connection_trouble), t.message.toString())
                    placeholderErrorImage.visibility = View.VISIBLE
                    placeholderMessage.setText(R.string.connection_trouble)
                    placeholderErrorImage.setImageResource(R.drawable.connecton_trouble)
                }
            })
    }

    companion object {
        lateinit var dataFromTextEdit: String
        private const val dataFromTextEditKey = "dataFromTextEdit"
    }
}
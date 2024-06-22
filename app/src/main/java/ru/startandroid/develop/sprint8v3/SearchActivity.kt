package ru.startandroid.develop.sprint8v3

import SearchHistory
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_HISTORY_SHARED_PREFERENCES = "search_history"

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener, Observer {
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
    private lateinit var placeholderErrorImage: ImageView
    private lateinit var buttonUpdate: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var cleanHistory: LinearLayout
    private lateinit var recentlyLookFor: TextView
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPrefs = getSharedPreferences(SEARCH_HISTORY_SHARED_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        searchHistory.addObserver(this)

        adapter = TrackAdapter(this)
        historyAdapter = TrackAdapter(this, searchHistory.loadHistoryTracks())

        cleanHistory = findViewById(R.id.clean_history)
        recentlyLookFor = findViewById(R.id.recently_look_for)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = historyAdapter

        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderErrorImage = findViewById(R.id.placeholderErrorImage)
        buttonUpdate = findViewById(R.id.update)
        hideSearchHistoryItems()


        cleanHistory.setOnClickListener {
            searchHistory.clearHistory()
            hideSearchHistoryItems()
        }

        dataFromTextEdit = savedInstanceState?.getString(dataFromTextEditKey) ?: ""

        val backFromSearch = findViewById<ImageView>(R.id.back_from_search)
        backFromSearch.setOnClickListener {
            finish()
        }

        update()

        editText = findViewById(R.id.edit_text)
        val clearEditText = findViewById<ImageView>(R.id.clear_text)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearEditText.visibility = View.INVISIBLE
                    historyAdapter.updateTracks(searchHistory.loadHistoryTracks())

                    recyclerView.adapter = historyAdapter
                    showSearchHistoryItems()

                } else {
                    clearEditText.visibility = View.VISIBLE
                    dataFromTextEdit = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        editText.addTextChangedListener(textWatcher)

        clearEditText.setOnClickListener {
            editText.setText("")
            dataFromTextEdit = ""
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
            hideErrorPlaceholder()
            buttonUpdate.visibility = View.GONE
            tracks.clear()

            recyclerView.adapter = historyAdapter
            update()
//            if (searchHistory.loadHistoryTracks().isEmpty()) {
//                hideSearchHistoryItems()
//            } else {
//                showSearchHistoryItems()
//            }
//            showHistory()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                hideSearchHistoryItems()
                recyclerView.adapter = adapter
                tracks.clear()
                adapter.updateTracks(tracks)
                true
            } else {
                showHistory()
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

    private fun hideSearchHistoryItems() {
        cleanHistory.visibility = View.GONE
        recentlyLookFor.visibility = View.GONE
        tracks.clear()
        historyAdapter.updateTracks(tracks)
    }

    private fun showSearchHistoryItems() {
        cleanHistory.visibility = View.VISIBLE
        recentlyLookFor.visibility = View.VISIBLE
        recyclerView.adapter = historyAdapter
    }

    private fun showErrorPlaceholder(text: Int, image: Int) {
        tracks.clear()
        adapter.updateTracks(tracks)
        recyclerView.visibility = View.GONE
        placeholderMessage.setText(text)
        placeholderMessage.visibility = View.VISIBLE
        placeholderErrorImage.setImageResource(image)
        placeholderErrorImage.visibility = View.VISIBLE
    }

    private fun hideErrorPlaceholder() {
        placeholderMessage.visibility = View.GONE
        placeholderErrorImage.visibility = View.GONE
    }

    private fun showViewHolder() {
        buttonUpdate.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        placeholderErrorImage.visibility = View.GONE
    }

    private fun search() {
        val query = editText.text.toString()
        placeholderErrorImage = findViewById(R.id.placeholderErrorImage)

        itunesService.search(query)
            .enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                adapter.updateTracks(tracks)
                                showViewHolder()
                            } else {
                                showErrorPlaceholder(
                                    R.string.nothing_found,
                                    R.drawable.nothings_found
                                )
                                buttonUpdate.visibility = View.GONE
                                tracks.clear()
                                tracks.addAll(response.body()?.results!!)
                                adapter.updateTracks(tracks)
                            }
                        }

                        else -> {
                            showErrorPlaceholder(
                                R.string.connection_trouble,
                                R.drawable.connecton_trouble
                            )
                            buttonUpdate.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    showErrorPlaceholder(R.string.connection_trouble, R.drawable.connecton_trouble)
                    buttonUpdate.visibility = View.VISIBLE
                    buttonUpdate.setOnClickListener { search() }
                }
            })
    }

    private fun showHistory() {
        cleanHistory.visibility = View.VISIBLE
        recentlyLookFor.visibility = View.VISIBLE
        recyclerView.adapter = historyAdapter
        historyAdapter.updateTracks(searchHistory.loadHistoryTracks())
    }

    companion object {
        lateinit var dataFromTextEdit: String
        private const val dataFromTextEditKey = "dataFromTextEdit"
    }

    override fun onClick(track: Track) {
        searchHistory.addToHistory(track)
        hideSearchHistoryItems()
        recyclerView.adapter = adapter

        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)

        intent.putExtra("selectedTrack", track)
        startActivity(intent)
    }

    override fun update() {
        if (searchHistory.isHistoryEmpty()) {
            hideSearchHistoryItems()
            if(editText.text.isNotEmpty()){
                Log.d("update", recyclerView.adapter.toString())
                recyclerView.adapter = adapter
                showViewHolder()
            }

        } else {
            showHistory()
        }
    }

    override fun onResume() {
update()
        super.onResume()
    }
}
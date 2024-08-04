package ru.startandroid.develop.sprint8v3.ui.Search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import ru.startandroid.develop.sprint8v3.Creator
import ru.startandroid.develop.sprint8v3.Observer
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.domain.api.HistoryInteractor
import ru.startandroid.develop.sprint8v3.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.ui.player.PlayerActivity
import ru.startandroid.develop.sprint8v3.ui.player.selectedTrack
import ru.startandroid.develop.sprint8v3.ui.tracks.TrackAdapter

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener, Observer {
    private val historyInteractor: HistoryInteractor by lazy { Creator.provideHistoryInteractor() }

    private lateinit var editText: EditText

    private val tracks = ArrayList<Track>()
    private lateinit var adapter: TrackAdapter
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderErrorImage: ImageView
    private lateinit var buttonUpdate: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var cleanHistory: LinearLayout
    private lateinit var recentlyLookFor: TextView
    private lateinit var backFromSearch: ImageView
    private lateinit var clearEditText: ImageView


    private val tracksInteractor: TracksInteractor by lazy { Creator.provideTracksInteractor() }


    private lateinit var progressBar: ProgressBar
    private val searchRunnable = Runnable {
        search()
              hideSearchHistoryItems()
        update()
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun setupViews() {

        cleanHistory = findViewById(R.id.clean_history)
        recentlyLookFor = findViewById(R.id.recently_look_for)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderErrorImage = findViewById(R.id.placeholderErrorImage)
        buttonUpdate = findViewById(R.id.update)
        backFromSearch = findViewById(R.id.back_from_search)
        editText = findViewById(R.id.edit_text)
        clearEditText = findViewById(R.id.clear_text)

        progressBar = findViewById(R.id.progress_bar)

        adapter = TrackAdapter(this)
        historyAdapter = TrackAdapter(this) //, historyInteractor.loadHistoryTracks())

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = historyAdapter

        val savedTracks = historyInteractor.loadHistoryTracks()
        historyAdapter.updateTracks(savedTracks)
        showHistory()
    }

    fun setupOnCLickListeners() {
        cleanHistory.setOnClickListener {
            historyInteractor.clearHistory()
            update()
        }

        backFromSearch.setOnClickListener {
            finish()
        }

        clearEditText.setOnClickListener {
            editText.setText("")
            dataFromTextEdit = ""
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
            hideErrorPlaceholder()
            buttonUpdate.visibility = View.GONE
            recyclerView.adapter = historyAdapter
            update()
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
                Toast.makeText(this, "entered to else", Toast.LENGTH_SHORT).show()
                showHistory()
                false
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupViews()
        setupOnCLickListeners()
        dataFromTextEdit = savedInstanceState?.getString(dataFromTextEditKey) ?: ""
        update()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    hideErrorPlaceholder()
                    dataFromTextEdit = ""
                    buttonUpdate.visibility = View.GONE
                    clearEditText.visibility = View.INVISIBLE
                    recyclerView.adapter = historyAdapter
                    update()
                } else {
                    clearEditText.visibility = View.VISIBLE
                    dataFromTextEdit = s.toString()
                    recyclerView.adapter = historyAdapter
                    showViewHolder()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        editText.addTextChangedListener(textWatcher)
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

    private fun showErrorPlaceholder(text: Int, image: Int) {
        progressBar.visibility = View.GONE
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
        progressBar.visibility = View.GONE
        buttonUpdate.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        placeholderErrorImage.visibility = View.GONE
    }

    private fun search() {
        progressBar.visibility = View.VISIBLE
        val query = editText.text.toString()

        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                runOnUiThread {
                    handleSearchResponse(foundTracks)
                }
            }

            override fun onError(error: Throwable) {
                runOnUiThread {
                    showErrorPlaceholder(R.string.connection_trouble, R.drawable.connecton_trouble)
                    buttonUpdate.visibility = View.VISIBLE
                    buttonUpdate.setOnClickListener { searchDebounce() }
                }
            }
        })
    }

    private fun handleSearchResponse(tracks: List<Track>) {
        progressBar.visibility = View.GONE
        if (tracks.isNotEmpty()) {
            this.tracks.clear()
            this.tracks.addAll(tracks)
            adapter.updateTracks(this.tracks)
            showViewHolder()
        } else {
            showErrorPlaceholder(R.string.nothing_found, R.drawable.nothings_found)
        }
    }

    private fun showHistory() {
        val updatedHistoryTracks = historyInteractor.loadHistoryTracks()

        historyAdapter.updateTracks(updatedHistoryTracks)
        progressBar.visibility = View.GONE
        cleanHistory.visibility = View.VISIBLE
        recentlyLookFor.visibility = View.VISIBLE
        recyclerView.adapter = historyAdapter
       historyAdapter.updateTracks(updatedHistoryTracks)
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            historyInteractor.addToHistory(track)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(selectedTrack, track)
            startActivity(intent)
        }
    }

    override fun update() {
        if (dataFromTextEdit.isNotEmpty()) {
            recyclerView.adapter = adapter
            adapter.updateTracks(tracks)
            showViewHolder()
        } else {
            if (historyInteractor.isHistoryEmpty()) {
                hideSearchHistoryItems()
            } else {
                showHistory()
            }
        }
    }

    override fun onResume() {
        update()
        super.onResume()
    }

    private fun searchDebounce() {
        progressBar.visibility = View.VISIBLE
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        lateinit var dataFromTextEdit: String
        private const val dataFromTextEditKey = "dataFromTextEdit"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
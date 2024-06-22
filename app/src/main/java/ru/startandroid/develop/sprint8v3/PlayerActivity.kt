package ru.startandroid.develop.sprint8v3

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
const val selectedTrack = "selectedTrack"
const val noData = "отсутствует"
class PlayerActivity : AppCompatActivity() {

    private lateinit var trackNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var releaseDate: TextView
    private lateinit var collectionName: TextView
    private lateinit var trackTime: TextView
    private lateinit var backFromPlayer: ImageView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        lookForViews()

        backFromPlayer.setOnClickListener{
            finish()
        }

        val selectedTrack = intent.getSerializableExtra(selectedTrack) as Track

        loadTrackInfo(selectedTrack)
    }

    private fun lookForViews() {
        backFromPlayer = findViewById(R.id.back_arrow)
        imageView = findViewById(R.id.artworkImageViewBig)
        trackNameTextView = findViewById(R.id.trackNameTextView)
        artistNameTextView = findViewById(R.id.artistNameTextView)
        primaryGenreName = findViewById(R.id.genreTextView)
        country = findViewById(R.id.countryTextView)
        releaseDate = findViewById(R.id.releaseDateTextView)
        collectionName = findViewById(R.id.collectionNameTextView)
        trackTime = findViewById(R.id.trackTimeTextView)
    }

    private fun loadTrackInfo(track: Track) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one)))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(imageView)

        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        primaryGenreName.text = track.primaryGenreName ?: noData
        country.text = track.country ?: noData

        val releaseDateString = track.releaseDate ?: noData
        if (releaseDateString == noData) {
            releaseDate.text = releaseDateString
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val date = dateFormat.parse(releaseDateString)
                val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                releaseDate.text = yearFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                releaseDate.text = noData
            }
        }

        collectionName.text = track.collectionName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
    }
}
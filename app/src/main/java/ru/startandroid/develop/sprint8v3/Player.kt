package ru.startandroid.develop.sprint8v3

import android.os.Bundle
import android.util.Log
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent

        val selectedTrack = intent.getSerializableExtra(selectedTrack) as Track

        val backFromPlayer = findViewById<ImageView>(R.id.back_arrow)

        backFromPlayer.setOnClickListener{
            finish()
        }

        val imageView = findViewById<ImageView>(R.id.artworkImageViewBig)

        Glide.with(this)
            .load(selectedTrack.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.small_one)))
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(imageView)

        val trackNameTextView = findViewById<TextView>(R.id.trackNameTextView)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val primaryGenreName = findViewById<TextView>(R.id.genreTextView)
        val country = findViewById<TextView>(R.id.countryTextView)
        val releaseDate = findViewById<TextView>(R.id.releaseDateTextView)
        val collectionName = findViewById<TextView>(R.id.collectionNameTextView)
        val trackTime = findViewById<TextView>(R.id.trackTimeTextView)


        trackNameTextView.text = selectedTrack.trackName
        artistNameTextView.text = selectedTrack.artistName
        primaryGenreName.text = selectedTrack.primaryGenreName ?: noData
        country.text = selectedTrack.country ?: noData


        val releaseDateString = selectedTrack.releaseDate ?: noData
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

        collectionName.text = selectedTrack.collectionName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(selectedTrack.trackTime)


    }
}
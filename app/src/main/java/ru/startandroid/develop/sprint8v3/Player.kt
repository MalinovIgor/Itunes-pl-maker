package ru.startandroid.develop.sprint8v3

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent

        val selectedTrack = intent.getSerializableExtra("selectedTrack") as Track

        val backFromPlayer = findViewById<ImageView>(R.id.back_arrow)

        backFromPlayer.setOnClickListener{
            finish()
        }


        Log.d("Click", selectedTrack.toString())

        val imageView = findViewById<ImageView>(R.id.artworkImageViewBig)

        Glide.with(this)
            .load(selectedTrack.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(8))
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
        primaryGenreName.text = selectedTrack.primaryGenreName ?: "отсутствует"
        country.text = selectedTrack.country ?: "отсутствует"


        val releaseDateString = selectedTrack.releaseDate ?: "отсутствует"
        if (releaseDateString == "отсутствует") {
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
                releaseDate.text = "отсутствует"
            }
        }

        collectionName.text = selectedTrack.collectionName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(selectedTrack.trackTime)


    }
}
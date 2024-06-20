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
import kotlin.math.log

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val intent = intent

        val selectedTrack = intent.getSerializableExtra("selectedTrack") as Track
    //    Toast.makeText(this,selectedTrack.trackName, Toast.LENGTH_SHORT).show()

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
        primaryGenreName.text = selectedTrack.primaryGenreName
        country.text = selectedTrack.country
        releaseDate.text = selectedTrack.releaseDate.toString()
        collectionName.text = selectedTrack.collectionName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(selectedTrack.trackTime)


    }
}
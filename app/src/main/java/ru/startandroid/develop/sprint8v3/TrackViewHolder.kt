package ru.startandroid.develop.sprint8v3

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.trackTimeTextView)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artworkImageView)

    fun bind(model: Track) {
        trackNameTextView.text = model.trackName
        artistNameTextView.text = model.artistName
        trackTimeTextView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .fitCenter()
            .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(artworkImageView)
    }
}
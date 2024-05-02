package ru.startandroid.develop.sprint8v3

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameTextView: TextView
    private val artistNameTextView: TextView
    private val trackTimeTextView: TextView
    private val artworkImageView: ImageView

    init {
        trackNameTextView = itemView.findViewById(R.id.trackNameTextView)
        artistNameTextView = itemView.findViewById(R.id.artistNameTextView)
        trackTimeTextView = itemView.findViewById(R.id.trackTimeTextView)
        artworkImageView = itemView.findViewById(R.id.artworkImageView)
    }


    fun bind(model: Track) {
        trackNameTextView.text = model.trackName
        artistNameTextView.text = model.artistName
        trackTimeTextView.text = model.trackTime
        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            //.apply(RequestOptions().placeholder(R.drawable.placeholder_image))
            .into(artworkImageView)
    }
}
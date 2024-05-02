package ru.startandroid.develop.sprint8v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.startandroid.develop.sprint8v3.TrackViewHolder
class TrackAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = trackList[position]
        holder.bind(currentTrack)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    // Вложенный класс TrackViewHolder
    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
              //  .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                .into(artworkImageView)
        }
    }
}
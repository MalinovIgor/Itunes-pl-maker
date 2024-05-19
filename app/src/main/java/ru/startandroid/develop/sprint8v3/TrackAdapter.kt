package ru.startandroid.develop.sprint8v3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.TrackViewHolder

class TrackAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>()
     {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = trackList[position]
        holder.bind(currentTrack)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}
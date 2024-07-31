package ru.startandroid.develop.sprint8v3.ui.tracks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.domain.models.Track

class TrackAdapter(val listener: Listener, private val tracks: ArrayList<Track> = ArrayList()) :
    RecyclerView.Adapter<TrackViewHolder>() {

    interface Listener {
        fun onClick(track: Track)
    }

    fun updateTracks(newTracks: List<Track>) {
        Log.d("TrackAdapter", "Before clearing: tracks size = ${tracks.size}")
        tracks.clear()
        Log.d("TrackAdapter", "After clearing: tracks size = ${tracks.size}")
        tracks.addAll(newTracks)
        Log.d("TrackAdapter", "After adding new tracks: tracks size = ${tracks.size}, newTracks size = ${newTracks.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int = tracks.size

    }


package ru.startandroid.develop.sprint8v3.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.data.dto.TrackDto
import ru.startandroid.develop.sprint8v3.domain.models.Track

class TrackAdapter(val listener: Listener, private val tracks: ArrayList<TrackDto> = ArrayList()) :
    RecyclerView.Adapter<TrackViewHolder>() {

    interface Listener {
        fun onClick(track: TrackDto)
    }

    fun updateTracks(newTracks: List<TrackDto>) {
        tracks.clear()
        tracks.addAll(newTracks)
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


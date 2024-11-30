package ru.startandroid.develop.sprint8v3.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class TrackAdapter(
    val listener: Listener,
    private val tracks: ArrayList<Track> = ArrayList(),
    private val onItemLongClickListener: OnItemLongClickListener? = null,
) : RecyclerView.Adapter<TrackViewHolder>() {
    fun getTracks(): List<Track> = tracks
    fun interface Listener {
        fun onClick(track: Track)
    }

    fun interface OnItemLongClickListener {
        fun onItemLongClick(item: Track)
    }

    fun updateTracks(newTracks: List<Track>) {
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
        holder.itemView.setOnClickListener { listener.onClick(tracks[position]) }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener.onItemLongClick(tracks[position])
                true
            }
        }
    }
    fun clearTracks() {
        tracks.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return tracks.size
    }
}
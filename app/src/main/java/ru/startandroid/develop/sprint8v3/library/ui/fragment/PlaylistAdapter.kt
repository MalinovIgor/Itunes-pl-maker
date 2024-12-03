package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track
import ru.startandroid.develop.sprint8v3.search.ui.TrackAdapter
class PlaylistAdapter(
    private val playlists: List<Playlist>,
    private val onItemClickListener: Listener
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(playlist)
        }
    }

    interface Listener {
        fun onClick(playlist: Playlist)
    }
}
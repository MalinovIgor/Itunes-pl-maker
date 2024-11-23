package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist

class PlaylistAdapter(private val playlists: List<Playlist>) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}
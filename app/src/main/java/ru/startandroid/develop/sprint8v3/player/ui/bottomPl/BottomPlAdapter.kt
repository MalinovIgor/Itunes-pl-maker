package ru.startandroid.develop.sprint8v3.player.ui.bottomPl

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.library.ui.fragment.PlaylistsViewHolder

class BottomPlAdapter(
    private val playlists: List<Playlist>,
    private val onItemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<BottomPlViewHolder>() {
    init {
        Log.d("testt", "Adapter created with ${playlists.size} items")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomPlViewHolder {
        Log.d("testt", "Holder created with ${viewType} viewtype")

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist_bottom, parent, false)
        return BottomPlViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomPlViewHolder, position: Int) {

        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(playlists[position]) }
    }

    fun interface OnItemClickListener {
        fun onItemClick(item: Playlist)
    }
}
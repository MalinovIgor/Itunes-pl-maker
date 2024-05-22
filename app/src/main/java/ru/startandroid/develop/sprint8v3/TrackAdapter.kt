package ru.startandroid.develop.sprint8v3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.startandroid.develop.sprint8v3.TrackViewHolder

class TrackAdapter(private val trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>()
     {
         var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    //{ хз, может быть надо будет это вернуть..
    //val itemView =
       //     LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
      //  return TrackViewHolder(itemView)
   // }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}
package ru.startandroid.develop.sprint8v3.library.ui.fragment

import android.app.Application
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.startandroid.develop.sprint8v3.R
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.ui.TrackAdapter
import java.io.File

class PlaylistsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = itemView.findViewById(R.id.title)
    private val description: TextView = itemView.findViewById(R.id.tracks_number)
    private val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image)
    fun bind(playlist: Playlist) {
        Log.d("PlaylistAdapter", "Binding playlist with ID: ${playlist.id}")
        if (!playlist.imagePath.isNullOrEmpty()) {
            playlistImage.scaleType = ImageView.ScaleType.CENTER_CROP
            val filePath =
                File(itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cache")
            val file = File(filePath, playlist.imagePath)
            val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.small_one)
            Glide.with(itemView)
                .load(file.toUri())
                .placeholder(R.drawable.placeholder_image)
                .apply(
                    RequestOptions().transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(cornerRadius)
                        )
                    )
                )
                .into(playlistImage)
        }
        title.text = playlist.name
        playlist.tracks.size.let {
            description.text = getPluralForm(it).format(it)
        }

    }

    fun getPluralForm(num: Int): String {
        if (num == 0) return itemView.context.getString(R.string.track_zero)
        else {
            val n = num % 100
            return when {
                n in 11..14 -> itemView.context.getString(R.string.tracks)
                n % 10 == 1 -> itemView.context.getString(R.string.track)
                n % 10 in 2..4 -> itemView.context.getString(R.string.tracks)
                else -> itemView.context.getString(R.string.tracks)
            }
        }
    }
}
package ru.startandroid.develop.sprint8v3.library.db.playlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String ="",
    val imagePath: String?,
    val tracks: String,
    val tracksCount: Int,
)
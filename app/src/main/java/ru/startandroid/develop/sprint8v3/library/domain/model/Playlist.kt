package ru.startandroid.develop.sprint8v3.library.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String?,
    val tracks: ArrayList<String>
)
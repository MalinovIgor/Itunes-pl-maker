package ru.startandroid.develop.sprint8v3.library.db.track

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_tracks_table")
data class TrackToPlEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    val addedAt: String,
    val isFavorite: Boolean
)
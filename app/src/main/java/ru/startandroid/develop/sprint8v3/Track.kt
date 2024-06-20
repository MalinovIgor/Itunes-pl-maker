package ru.startandroid.develop.sprint8v3

import com.google.gson.annotations.SerializedName
import java.io.Serializable



data class Track (
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val trackId : String,
    val collectionName : String = "-",
    val releaseDate : Int = 0,
    val primaryGenreName : String = "-",
    val country : String = "-"
) : Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun withDefaultValues(): Track {
        return this.copy(
            collectionName = collectionName ?: "-",
            releaseDate = releaseDate ?: 0,
            primaryGenreName = primaryGenreName ?: "-",
            country = country ?: "-"
        )
    }
}
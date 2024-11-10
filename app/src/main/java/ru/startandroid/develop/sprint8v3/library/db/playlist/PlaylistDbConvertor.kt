package ru.startandroid.develop.sprint8v3.library.db.playlist

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.startandroid.develop.sprint8v3.library.db.track.TrackEntity
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            tracks = createTracksFromJson(playlist.tracks),
        )
    }

    fun createTracksFromJson(json: String): ArrayList<String> {
        if (json == "") return ArrayList()
        val trackListType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, trackListType)
    }

    fun createJsonFromTracks(tracks: ArrayList<String>): String {
        return Gson().toJson(tracks)
    }


    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            tracks = createJsonFromTracks(playlist.tracks),
            tracksCount = playlist.tracks.size
        )
    }
}
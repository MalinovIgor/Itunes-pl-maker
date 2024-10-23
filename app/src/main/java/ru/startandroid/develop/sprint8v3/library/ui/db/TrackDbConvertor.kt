package ru.startandroid.develop.sprint8v3.library.ui.db

import ru.startandroid.develop.sprint8v3.search.data.dto.TrackDto
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class TrackDbConvertor {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }
}
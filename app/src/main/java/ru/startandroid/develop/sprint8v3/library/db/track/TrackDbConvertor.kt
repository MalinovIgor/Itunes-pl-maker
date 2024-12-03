package ru.startandroid.develop.sprint8v3.library.db.track

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class TrackDbConvertor {

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorites = true
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            addedTime = System.currentTimeMillis().toString(),
            track.isFavorites
            )
    }
}
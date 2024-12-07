package ru.startandroid.develop.sprint8v3.library.db.track

import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class TrackToPlDbConvertor {
    fun map(track: Track): TrackToPlEntity {
        return TrackToPlEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl.toString(),
            collectionName = track.collectionName.toString(),
            primaryGenreName = track.primaryGenreName ?: "unknown",
            releaseDate = track.releaseDate.orEmpty(),
            country = track.country.toString(),
            addedAt = System.currentTimeMillis().toString(),
            isFavorite = track.isFavorites
        )
    }

    fun map(trackEntity: TrackToPlEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            previewUrl = trackEntity.previewUrl,
            collectionName = trackEntity.collectionName,
            primaryGenreName = trackEntity.primaryGenreName,
            releaseDate = trackEntity.releaseDate,
            country = trackEntity.country,
            isFavorites = trackEntity.isFavorite
        )
    }
}
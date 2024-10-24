package ru.startandroid.develop.sprint8v3.library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.library.db.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.TrackDbConvertor
import ru.startandroid.develop.sprint8v3.library.db.TrackEntity
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.search.data.dto.TrackDto
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
            }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.trackDao().getFavoritesTracks()
        emit(convertFromTrackEntity(favoriteTracks))
    }

    fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}
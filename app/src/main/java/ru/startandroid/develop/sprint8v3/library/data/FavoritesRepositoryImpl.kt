package ru.startandroid.develop.sprint8v3.library.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.library.db.track.AppDatabase
import ru.startandroid.develop.sprint8v3.library.db.track.TrackDbConvertor
import ru.startandroid.develop.sprint8v3.library.db.track.TrackEntity
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(track.trackId)
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return flow {
            val favoriteTracks = appDatabase.trackDao().getFavoritesTracks()
            emit(convertFromTrackEntity(favoriteTracks))
        }
    }

    override suspend fun isFavorite(trackId: String): Boolean {
        return appDatabase.trackDao().isFavorite(trackId)
    }

    fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}
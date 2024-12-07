package ru.startandroid.develop.sprint8v3.library.domain.db

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface FavoritesRepository {
    suspend fun isFavorite(trackId: String): Boolean
    suspend fun addTrackToFavorites(track: Track)
    suspend fun deleteTrackFromFavorites(track: Track)
    suspend fun getFavoritesTracks(): Flow<List<Track>>
}
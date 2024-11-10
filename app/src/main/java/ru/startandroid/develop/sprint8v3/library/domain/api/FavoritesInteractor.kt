package ru.startandroid.develop.sprint8v3.library.domain.api

import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

interface FavoritesInteractor {
    suspend fun addTrackToFavorites(track:Track){

    }
    suspend fun deleteTrackFromFavorites(track:Track)
    suspend fun getFavoritesTracks(): Flow<List<Track>>
}
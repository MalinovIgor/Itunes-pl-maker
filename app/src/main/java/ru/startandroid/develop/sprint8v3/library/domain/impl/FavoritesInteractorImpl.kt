package ru.startandroid.develop.sprint8v3.library.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.startandroid.develop.sprint8v3.di.repositoryModule
import ru.startandroid.develop.sprint8v3.library.domain.api.FavoritesInteractor
import ru.startandroid.develop.sprint8v3.library.domain.db.FavoritesRepository
import ru.startandroid.develop.sprint8v3.search.domain.models.Track

class FavoritesInteractorImpl(private val repository: FavoritesRepository):FavoritesInteractor {
    override suspend fun deleteTrackFromFavorites(track: Track) {
        repository.deleteTrackFromFavorites(track)

    }    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
       return (repository.getFavoritesTracks())
    }
}
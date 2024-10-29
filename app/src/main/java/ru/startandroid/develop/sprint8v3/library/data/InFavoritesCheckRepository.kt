package ru.startandroid.develop.sprint8v3.library.data

import ru.startandroid.develop.sprint8v3.library.db.AppDatabase

class InFavoritesCheckRepository (private val appDatabase: AppDatabase) {
    suspend fun isInFavorites(id: String): Boolean {
        val favorites = appDatabase.trackDao().getLibraryTracksId().first()
        return favorites.contains(id)
    }
}
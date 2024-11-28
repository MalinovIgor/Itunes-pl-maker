package ru.startandroid.develop.sprint8v3.library.db.track

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.startandroid.develop.sprint8v3.library.db.dao.PlaylistDao
import ru.startandroid.develop.sprint8v3.library.db.dao.TrackDao
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity

@Database(
    version = 3,
    entities = [
        TrackEntity::class, PlaylistEntity::class,
    ],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}
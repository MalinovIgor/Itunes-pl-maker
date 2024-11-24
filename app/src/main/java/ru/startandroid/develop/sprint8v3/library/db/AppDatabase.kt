package ru.startandroid.develop.sprint8v3.library.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.startandroid.develop.sprint8v3.library.db.dao.TrackDao

@Database(
    version = 2,
    entities = [
       TrackEntity::class
    ]
)
abstract class AppDatabase:RoomDatabase() {

    abstract fun trackDao(): TrackDao
}
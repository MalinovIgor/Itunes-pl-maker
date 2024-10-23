package ru.startandroid.develop.sprint8v3.library.ui.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.startandroid.develop.sprint8v3.library.ui.db.TrackEntity

interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table")
    suspend fun getLibraryTracksId(): List<String>          // TODO

    @Delete
    suspend fun deleteTrack(tracks: List<TrackEntity>)      // TODO
}
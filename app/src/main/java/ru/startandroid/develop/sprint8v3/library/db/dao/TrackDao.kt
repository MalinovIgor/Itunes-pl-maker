package ru.startandroid.develop.sprint8v3.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.startandroid.develop.sprint8v3.library.db.track.TrackEntity
@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addedTime DESC")
    suspend fun getFavoritesTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM track_table")
    suspend fun getLibraryTracksId(): List<String>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Update(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateTracks(trackEntity: TrackEntity)
}
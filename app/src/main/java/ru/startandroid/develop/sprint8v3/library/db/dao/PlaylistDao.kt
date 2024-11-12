package ru.startandroid.develop.sprint8v3.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.db.track.TrackEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

//    @Query("SELECT id FROM playlist_table")
//    suspend fun getPlaylistById(): PlaylistEntity

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylists(playlistEntity: PlaylistEntity)

}
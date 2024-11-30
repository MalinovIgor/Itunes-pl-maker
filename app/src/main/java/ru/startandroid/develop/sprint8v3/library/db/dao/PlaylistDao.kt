package ru.startandroid.develop.sprint8v3.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.startandroid.develop.sprint8v3.library.db.playlist.PlaylistEntity
import ru.startandroid.develop.sprint8v3.library.db.track.TrackEntity
import ru.startandroid.develop.sprint8v3.library.db.track.TrackToPlEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    fun getPlaylistById(id: Int): PlaylistEntity

    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylists(playlistEntity: PlaylistEntity)

    @Query("SELECT tracks FROM playlist_table WHERE id = :playlistId")
    suspend fun getAllTracksFromPlaylist(playlistId: Int): String

    @Query("SELECT * FROM all_tracks_table")
    fun getAllTracks(): List<TrackToPlEntity>

    @Query("SELECT * FROM all_tracks_table WHERE trackId IN (:trackIds)")
    suspend fun getTrackByIds(trackIds: List<String>): List<TrackToPlEntity>

    @Insert(entity = TrackToPlEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: TrackToPlEntity)

    @Query("DELETE FROM all_tracks_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)
}
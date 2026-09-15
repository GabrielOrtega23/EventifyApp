package com.gabriel.eventify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabriel.eventify.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    @Query("SELECT * FROM events ORDER BY id ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE category = :category ORDER BY id ASC")
    fun getEventsByCategory(category: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchEvents(query: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: Int): EventEntity?

    @Query("SELECT COUNT(*) FROM events")
    suspend fun countEvents(): Int
}

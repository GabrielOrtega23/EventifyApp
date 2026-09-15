package com.gabriel.eventify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gabriel.eventify.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Insert
    suspend fun insertReservation(reservation: ReservationEntity): Long

    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getReservationsForUser(userId: Int): Flow<List<ReservationEntity>>
}

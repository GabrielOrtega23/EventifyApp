package com.gabriel.eventify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val userId: Int,
    val seats: Int,
    val totalPrice: Double,
    val timestamp: Long
)

package com.gabriel.eventify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val price: Double,
    val imageUrl: String,
    val capacity: Int,
    val badge: String = ""
)

package com.gabriel.eventify.data.repository

import com.gabriel.eventify.data.local.dao.EventDao
import com.gabriel.eventify.data.local.dao.ReservationDao
import com.gabriel.eventify.data.local.entity.EventEntity
import com.gabriel.eventify.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao,
    private val reservationDao: ReservationDao
) {
    fun getEvents(category: String, query: String): Flow<List<EventEntity>> {
        return when {
            query.isNotBlank() -> eventDao.searchEvents(query)
            category != "all" -> eventDao.getEventsByCategory(category)
            else -> eventDao.getAllEvents()
        }
    }

    suspend fun getEventById(id: Int): EventEntity? = eventDao.getEventById(id)

    suspend fun reserveSeats(eventId: Int, userId: Int, seats: Int, totalPrice: Double) {
        reservationDao.insertReservation(
            ReservationEntity(
                eventId = eventId,
                userId = userId,
                seats = seats,
                totalPrice = totalPrice,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getReservationsForUser(userId: Int): Flow<List<ReservationEntity>> =
        reservationDao.getReservationsForUser(userId)
}

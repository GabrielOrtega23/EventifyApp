package com.gabriel.eventify

import android.app.Application
import com.gabriel.eventify.data.local.AppDatabase
import com.gabriel.eventify.data.repository.AuthRepository
import com.gabriel.eventify.data.repository.EventRepository

class EventifyApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var eventRepository: EventRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        authRepository = AuthRepository(database.userDao())
        eventRepository = EventRepository(database.eventDao(), database.reservationDao())
    }
}

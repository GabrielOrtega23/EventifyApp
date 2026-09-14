package com.gabriel.eventify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabriel.eventify.data.repository.AuthRepository
import com.gabriel.eventify.data.repository.EventRepository
import com.gabriel.eventify.ui.screens.auth.AuthViewModel
import com.gabriel.eventify.ui.screens.eventdetail.EventDetailViewModel
import com.gabriel.eventify.ui.screens.home.HomeViewModel

class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val eventRepository: EventRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(authRepository) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(eventRepository) as T

            modelClass.isAssignableFrom(EventDetailViewModel::class.java) ->
                EventDetailViewModel(eventRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

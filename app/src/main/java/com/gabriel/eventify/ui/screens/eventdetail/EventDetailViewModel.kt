package com.gabriel.eventify.ui.screens.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.eventify.data.local.entity.EventEntity
import com.gabriel.eventify.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EventDetailUiState(
    val event: EventEntity? = null,
    val seatCount: Int = 1,
    val isLoading: Boolean = true,
    val reservationConfirmed: Boolean = false
)

class EventDetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState

    fun loadEvent(eventId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val event = repository.getEventById(eventId)
            _uiState.value = _uiState.value.copy(event = event, isLoading = false)
        }
    }

    fun incrementSeats() {
        val current = _uiState.value
        val max = current.event?.capacity ?: Int.MAX_VALUE
        if (current.seatCount < max) {
            _uiState.value = current.copy(seatCount = current.seatCount + 1)
        }
    }

    fun decrementSeats() {
        val current = _uiState.value
        if (current.seatCount > 1) {
            _uiState.value = current.copy(seatCount = current.seatCount - 1)
        }
    }

    fun reserve(userId: Int) {
        val current = _uiState.value
        val event = current.event ?: return
        viewModelScope.launch {
            repository.reserveSeats(
                eventId = event.id,
                userId = userId,
                seats = current.seatCount,
                totalPrice = event.price * current.seatCount
            )
            _uiState.value = current.copy(reservationConfirmed = true)
        }
    }

    fun dismissConfirmation() {
        _uiState.value = _uiState.value.copy(reservationConfirmed = false)
    }
}

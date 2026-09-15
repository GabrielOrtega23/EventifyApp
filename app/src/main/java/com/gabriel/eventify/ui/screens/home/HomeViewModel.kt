package com.gabriel.eventify.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.eventify.data.local.entity.EventEntity
import com.gabriel.eventify.data.repository.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(repository: EventRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory

    val categories = listOf("all", "music", "theater", "sports", "workshop")

    val events: StateFlow<List<EventEntity>> = combine(_searchQuery, _selectedCategory) { query, category ->
        query to category
    }.flatMapLatest { (query, category) ->
        repository.getEvents(category, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }
}

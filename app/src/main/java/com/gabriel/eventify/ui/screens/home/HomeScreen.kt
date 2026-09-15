package com.gabriel.eventify.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gabriel.eventify.R
import com.gabriel.eventify.ui.components.EventCard
import com.gabriel.eventify.ui.components.categoryLabel
import com.gabriel.eventify.ui.theme.EventifyGradientEnd
import com.gabriel.eventify.ui.theme.EventifyGradientStart

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onEventClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val events by viewModel.events.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_eventify_icon),
                contentDescription = "Eventify",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Encontre seu próximo evento",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Sair",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar eventos, locais...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EventifyGradientStart,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = EventifyGradientStart
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { viewModel.onCategorySelected(category) },
                    label = { Text(categoryLabel(category).let { if (category == "all") "Todos" else it }) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EventifyGradientStart,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Nenhum evento encontrado.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 40.dp)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(events, key = { it.id }) { event ->
                    EventCard(event = event, onClick = { onEventClick(event.id) })
                }
            }
        }
    }
}

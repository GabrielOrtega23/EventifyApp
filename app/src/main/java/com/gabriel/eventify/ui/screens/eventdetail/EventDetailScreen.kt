package com.gabriel.eventify.ui.screens.eventdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gabriel.eventify.ui.components.GradientButton
import com.gabriel.eventify.ui.components.categoryLabel
import com.gabriel.eventify.ui.theme.EventifyGradientEnd
import com.gabriel.eventify.ui.theme.EventifyGradientStart

@Composable
fun EventDetailScreen(
    eventId: Int,
    userId: Int,
    viewModel: EventDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    val event = uiState.event

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (event != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = event.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EventifyGradientStart)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = categoryLabel(event.category),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = event.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(event.location, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("${event.date} • ${event.time}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Sobre o evento", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(event.description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Ingressos", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (event.price > 0) "$ ${"%.2f".format(event.price)} / assento" else "Gratuito",
                            color = EventifyGradientEnd,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.decrementSeats() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = "Diminuir", tint = Color.White)
                            }
                            Text(
                                text = "${uiState.seatCount}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            IconButton(
                                onClick = { viewModel.incrementSeats() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Aumentar", tint = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    GradientButton(
                        text = "Reservar assento • $ ${"%.2f".format(event.price * uiState.seatCount)}",
                        onClick = { viewModel.reserve(userId) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        if (uiState.reservationConfirmed) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissConfirmation() },
                icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = EventifyGradientStart) },
                title = { Text("Reserva confirmada!") },
                text = { Text("Seus ${uiState.seatCount} assento(s) para \"${event?.title}\" foram reservados com sucesso.") },
                confirmButton = {
                    Text(
                        text = "Fechar",
                        color = EventifyGradientStart,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable {
                                viewModel.dismissConfirmation()
                                onBack()
                            }
                    )
                }
            )
        }
    }
}

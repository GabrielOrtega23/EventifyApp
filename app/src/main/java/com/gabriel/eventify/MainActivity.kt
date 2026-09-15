package com.gabriel.eventify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gabriel.eventify.ui.ViewModelFactory
import com.gabriel.eventify.ui.navigation.EventifyNavGraph
import com.gabriel.eventify.ui.theme.EventifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as EventifyApp
        val viewModelFactory = ViewModelFactory(app.authRepository, app.eventRepository)

        setContent {
            EventifyTheme {
                EventifyNavGraph(viewModelFactory = viewModelFactory)
            }
        }
    }
}

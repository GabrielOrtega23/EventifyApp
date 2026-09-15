package com.gabriel.eventify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabriel.eventify.ui.ViewModelFactory
import com.gabriel.eventify.ui.screens.auth.AuthViewModel
import com.gabriel.eventify.ui.screens.auth.LoginScreen
import com.gabriel.eventify.ui.screens.auth.SignUpScreen
import com.gabriel.eventify.ui.screens.eventdetail.EventDetailScreen
import com.gabriel.eventify.ui.screens.eventdetail.EventDetailViewModel
import com.gabriel.eventify.ui.screens.home.HomeScreen
import com.gabriel.eventify.ui.screens.home.HomeViewModel
import com.gabriel.eventify.ui.screens.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val EVENT_DETAIL = "event_detail/{eventId}"

    fun eventDetail(eventId: Int) = "event_detail/$eventId"
}

@Composable
fun EventifyNavGraph(viewModelFactory: ViewModelFactory) {
    val navController = rememberNavController()
    var currentUserId by remember { mutableIntStateOf(-1) }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Routes.LOGIN) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                viewModel = authViewModel,
                onBack = { },
                onLoginSuccess = { user ->
                    currentUserId = user.id
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Routes.SIGNUP) }
            )
        }

        composable(Routes.SIGNUP) {
            val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            SignUpScreen(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSignUpSuccess = { user ->
                    currentUserId = user.id
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            HomeScreen(
                viewModel = homeViewModel,
                onEventClick = { eventId -> navController.navigate(Routes.eventDetail(eventId)) },
                onLogout = {
                    currentUserId = -1
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: -1
            val detailViewModel: EventDetailViewModel = viewModel(factory = viewModelFactory)
            EventDetailScreen(
                eventId = eventId,
                userId = currentUserId,
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

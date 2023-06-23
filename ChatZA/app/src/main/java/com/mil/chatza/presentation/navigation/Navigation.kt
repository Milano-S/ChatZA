package com.mil.chatza.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.presentation.screens.LoginScreen
import com.mil.chatza.presentation.screens.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        // Main Screen
        composable(Screen.LoginPage.route) {
            LoginScreen(navController)
        }
    }
}
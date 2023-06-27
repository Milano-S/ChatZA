package com.mil.chatza.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.presentation.screens.LoginScreen
import com.mil.chatza.presentation.screens.RegisterScreen
import com.mil.chatza.presentation.screens.SplashScreen
import com.mil.chatza.presentation.screens.VerifyEmailScreen
import com.mil.chatza.presentation.viewmodels.AuthViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val authVM : AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashPage.route
    ) {
        //Splash
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        //Login
        composable(Screen.LoginPage.route) {
            LoginScreen(navController = navController, authVM = authVM)
        }

        //Register
        composable(Screen.RegisterPage.route) {
            RegisterScreen(navController = navController, authVM = authVM)
        }

        //Register
        composable(Screen.VerifyEmailPage.route) {
            VerifyEmailScreen(navController = navController, authVM = authVM)
        }

    }
}
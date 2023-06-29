package com.mil.chatza.presentation.navigation

import HomePage
import HomeScreen
import ProfileScreen
import SearchScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.presentation.screens.CreateProfileScreen
import com.mil.chatza.presentation.screens.DisclaimerScreen
import com.mil.chatza.presentation.screens.LoginScreen
import com.mil.chatza.presentation.screens.RegisterScreen
import com.mil.chatza.presentation.screens.SplashScreen
import com.mil.chatza.presentation.screens.VerifyEmailScreen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authVM: AuthViewModel,
    firebaseViewModel: FirebaseViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Consts.Companion.Graph.AUTH,
    ) {
        //Auth Nav
        navigation(
            route = Consts.Companion.Graph.MAIN,
            startDestination = Screen.HomePage.route
        ) {
            //Home Page
            composable(Screen.HomePage.route) {
                HomePage(navController = navController)
            }

            //Home Screen
            composable(Screen.HomeScreen.route){
                HomeScreen()
            }

            composable(Screen.SettingsPage.route){
                ProfileScreen()
            }

            composable(Screen.AboutPage.route){
                SearchScreen()
            }
        }

        //Auth Nav
        navigation(
            route = Consts.Companion.Graph.AUTH,
            startDestination = "splash_screen"
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

            //VerifyEmail
            composable(Screen.VerifyEmailPage.route) {
                VerifyEmailScreen(navController = navController, authVM = authVM)
            }

            //Create Profile
            composable(Screen.CreateProfilePage.route) {
                CreateProfileScreen(
                    navController = navController,
                    authVM = authVM,
                    firebaseVM = firebaseViewModel
                )
            }

            //Disclaimer
            composable(Screen.DisclaimerPage.route) {
                DisclaimerScreen(navController = navController)
            }

        }
    }
}
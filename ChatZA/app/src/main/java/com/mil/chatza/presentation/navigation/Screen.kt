package com.mil.chatza.presentation.navigation

sealed class Screen(
    val route : String
) {
    object SplashPage: Screen("splash_screen")
    object LoginPage: Screen("login_screen")
    object RegisterPage : Screen("register_screen")
    object VerifyEmailPage : Screen("verify_email_screen")
}
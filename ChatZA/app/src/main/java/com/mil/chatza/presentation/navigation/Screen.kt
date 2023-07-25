package com.mil.chatza.presentation.navigation

sealed class Screen(
    val route : String
) {
    object SplashPage: Screen("splash_screen")
    object LoginPage: Screen("login_screen")
    object RegisterPage : Screen("register_screen")
    object VerifyEmailPage : Screen("verify_email_screen")
    object CreateProfilePage : Screen("create_profile_page")
    object DisclaimerPage : Screen("disclaimer_page")
    object HomePage : Screen("home_page")
    object HomeScreen : Screen("Home")
    object SettingsScreen : Screen("settings_page")
    object ProfileScreen : Screen("Profile")
    object ForgotPasswordScreen : Screen("forgot_password_screens")
    object EditProfileScreen : Screen("edit_profile_screen")
}
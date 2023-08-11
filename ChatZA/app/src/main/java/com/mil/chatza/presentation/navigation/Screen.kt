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
    object ChatScreen : Screen("Chat")
    object FriendsScreen : Screen("Friends")
    object ProfileScreen : Screen("Profile")
    object HelpScreen : Screen("help_screen")
    object ForgotPasswordScreen : Screen("forgot_password_screens")
    object EditProfileScreen : Screen("edit_profile_screen")
    object ChatDetailsScreen : Screen("chat_screen")
}
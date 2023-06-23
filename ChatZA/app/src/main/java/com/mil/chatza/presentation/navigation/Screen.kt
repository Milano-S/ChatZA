package com.mil.chatza.presentation.navigation

sealed class Screen(
    val route : String
) {
    object LoginPage: Screen("login_screen")
}
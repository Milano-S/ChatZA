package com.mil.chatza.domain.model


sealed class LoginResult
data class SuccessLogin(val value: Boolean) : LoginResult()
data class FailureLogin(val exception: Exception) : LoginResult()

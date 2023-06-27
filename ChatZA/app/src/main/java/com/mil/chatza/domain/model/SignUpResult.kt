package com.mil.chatza.domain.model

sealed class SignUpResult
data class Success(val value: Boolean) : SignUpResult()
data class Failure(val exception: Exception) : SignUpResult()

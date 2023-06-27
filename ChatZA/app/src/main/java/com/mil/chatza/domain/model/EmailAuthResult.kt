package com.mil.chatza.domain.model

sealed class EmailAuthResult
data class SuccessEmailAuth(val value: Boolean) : EmailAuthResult()
data class FailureEmailAuth(val exception: Exception) : EmailAuthResult()

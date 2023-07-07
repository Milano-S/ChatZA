package com.mil.chatza.domain.model

sealed class GsUrlResult
data class SuccessGsUrl(val value: Boolean) : GsUrlResult()
data class FailureGsUrl(val exception: Exception) : GsUrlResult()

package com.mil.chatza.domain.model

sealed class UploadUserResult
data class SuccessUserUpload(val value: Boolean) : UploadUserResult()
data class FailureUserUpload(val exception: Exception) : UploadUserResult()

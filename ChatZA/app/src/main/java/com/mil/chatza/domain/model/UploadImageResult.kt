package com.mil.chatza.domain.model

sealed class UploadImageResult
data class SuccessImageUpload(val value: Boolean) : UploadImageResult()
data class FailureImageUpload(val exception: Exception) : UploadImageResult()

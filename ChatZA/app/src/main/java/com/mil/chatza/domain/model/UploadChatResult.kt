package com.mil.chatza.domain.model

sealed class UploadChatResult
data class SuccessChatUpload(val value: Boolean) : UploadChatResult()
data class FailureChatUpload(val exception: Exception) : UploadChatResult()

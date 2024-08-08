package com.mil.chatza.domain.model

import java.util.Calendar
import java.util.UUID

data class Message(
    val sender: UserProfile = UserProfile(),
    val message: String = "",
    val time: String = Calendar.getInstance().timeInMillis.toString()
)
package com.mil.chatza.domain.model

data class Chat(
    val chatName: String,
    val participants: List<UserProfile>,
    val lastMessage: String,
    val isPrivate : Boolean,
    val messages: List<Message>
) {
    constructor() : this("", emptyList(), "", false, emptyList())
}


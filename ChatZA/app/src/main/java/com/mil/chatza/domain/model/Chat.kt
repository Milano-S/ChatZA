package com.mil.chatza.domain.model

data class Chat(
    val chatName: String = "",
    val participants: List<UserProfile> = emptyList(),
    val lastMessage: String = "",
    val isPrivate: Boolean = false,
    val messages: List<Message> = emptyList(),
    val chatCreator: UserProfile = UserProfile(),
    var isFriendChat: Boolean = false
)


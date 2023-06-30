package com.mil.chatza.domain.model

data class UserProfile(
    val dateCreated : String = "",
    val email : String = "",
    val name : String = "",
    val age : String = "",
    val gender : String = "",
    val province : String = "",
    val profileImageUrl : String = ""
)
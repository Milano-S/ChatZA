package com.mil.chatza.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import com.mil.chatza.domain.model.BottomNavItem

class Consts {

    companion object {

        //ChatZa Email
        const val chatZaEmail = "chatzaapp@gmail.com"

        //Firebase Collections
        const val users = "users"
        const val chats = "chats"
        const val profileImages = "images"

        //Nav Graphs
        object Graph {
            const val AUTH = "auth_graph"
            const val MAIN = "main_graph"
        }

        //Gender
        val genderList = listOf("Male", "Female", "Other")

        //Province List
        val provinceList = listOf(
            "Gauteng",
            "KwaZulu - Natal",
            "Western Cape",
            "Eastern Cape",
            "Limpopo",
            "Mpumalanga",
            "North West",
            "Free State",
            "Northern Cape",
        )

    }

}
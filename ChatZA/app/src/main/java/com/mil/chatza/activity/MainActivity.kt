package com.mil.chatza.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mil.chatza.presentation.navigation.Navigation
import com.mil.chatza.ui.theme.ChatZATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatZATheme {
                    Navigation()
            }
        }
    }
}

package com.mil.chatza.presentation.screens

import android.content.res.Resources.Theme
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.navigation.NavController
import com.mil.chatza.R
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.ui.theme.ChatZATheme
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val splashBackGroundColor = chatZaBrown

    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        delay(1000)
        navController.navigate(Screen.LoginPage.route)
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(splashBackGroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
   /* val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    // Animation
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            // tween Animation
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        // Customize the delay time
        delay(3000L)
        navController.navigate(Screen.LoginPage.route)
    }

    // Image
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Change the logo
        Image(
            painter = painterResource(id = R.drawable.zaflag),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }*/
}
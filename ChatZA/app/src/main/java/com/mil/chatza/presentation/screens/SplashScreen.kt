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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
        delay(500)
        navController.navigate(Screen.LoginPage.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(chatZaBrown)
    ) {
        Image(
            painter = painterResource(id = R.drawable.people),
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSplash() {
    SplashScreen(navController = rememberNavController())
}
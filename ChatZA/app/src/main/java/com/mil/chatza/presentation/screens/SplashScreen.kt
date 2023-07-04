package com.mil.chatza.presentation.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.R
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


private const val TAG = "SplashScreen"

@Composable
fun SplashScreen(
    navController: NavController,
    authVM: AuthViewModel,
    firebaseVM: FirebaseViewModel
) {

    val scope = rememberCoroutineScope()
    val currentContext = LocalContext.current
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
        scope.launch {
            try {
                val currentUser = authVM.auth.currentUser
                //User Exists, Verified and has Profile
                if (currentUser != null && currentUser.isEmailVerified && firebaseVM.getProfileDetails(currentUser.email.toString()).name != "") {
                    navController.navigate(Consts.Companion.Graph.MAIN)
                }else{
                    navController.navigate(Screen.LoginPage.route)
                }
            } catch (e: Exception) {
                Log.i(TAG, e.message.toString())
                Toast.makeText(currentContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.LoginPage.route)
            }
        }
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
    //SplashScreen(navController = rememberNavController())
}
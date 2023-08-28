package com.mil.chatza.presentation.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.R
import com.mil.chatza.domain.model.SuccessEmailAuth
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


private const val TAG = "VerifyEmailScreen"

@Composable
fun VerifyEmailScreen(navController: NavHostController, authVM: AuthViewModel) {

    val scope = rememberCoroutineScope()
    val currentContext = LocalContext.current

    var progressBarState by remember { mutableStateOf(false) }

    //Send Authentication Email
    LaunchedEffect(Unit) {
        if (authVM.authenticateEmail() != SuccessEmailAuth(true)) {
            Toast.makeText(
                currentContext,
                authVM.emailAuthException.value?.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //Back Button
        BackHandler(enabled = true) {
            if (currentContext is Activity) {
                currentContext.moveTaskToBack(true)
            }
        }

        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(chatZaBrown, Color.White, chatZaBrown),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .fillMaxSize()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(115.dp))

            Text(
                "Verify your Email",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "You will need to verify your email\nto complete registration",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable {
                        progressBarState = true
                        scope.launch {
                            progressBarState =
                                if (authVM.authenticateEmail() != SuccessEmailAuth(true)) {
                                    Toast
                                        .makeText(
                                            currentContext,
                                            authVM.emailAuthException.value?.message.toString(),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    false
                                } else {
                                    Toast
                                        .makeText(
                                            currentContext,
                                            "Email Has Been Resent",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    false
                                }
                        }
                    },
                text = "Resend Email",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = chatZaBlue
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.wrapContentSize()) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(id = R.drawable.email_veri_modified),
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "An email has been sent to you with a link to verify your account",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                text = "If you have not received an email after a few minutes, please check your spam folder",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    repeat(3) {
                        scope.launch {
                            authVM.auth.currentUser!!.reload().await()
                        }
                        val toastText = if (authVM.auth.currentUser!!.isEmailVerified) "Email Authenticated" else "Not Authenticated"
                        Toast.makeText(currentContext, toastText, Toast.LENGTH_SHORT).show()
                        if (authVM.auth.currentUser!!.isEmailVerified) {
                            navController.navigate(Screen.CreateProfilePage.route)
                        }
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)
                    .height(50.dp)
                    .border(
                        border = BorderStroke(width = 0.5.dp, color = Color.DarkGray),
                        shape = RoundedCornerShape(
                            50.dp
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = chatZaBrown)
            ) {
                Text(text = "Proceed", fontSize = 15.sp, color = Color.DarkGray)
            }

        }
        when (progressBarState) {
            true -> ProgressBar()
            else -> {}
        }
    }
}


@Composable
@Preview()
private fun PreviewEmailVerify() {
    //VerifyEmailScreen(navController = rememberNavController(), authVM = AuthViewModel())
}
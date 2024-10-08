package com.mil.chatza.presentation.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.R
import com.mil.chatza.domain.model.Response
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.ui.theme.chatZaBrown

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    authVM: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var forgotPasswordEmail by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }

    fun validateEmail(): Boolean {
        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(forgotPasswordEmail).matches()
        return isEmailError
    }

    fun showMessage(
        context: Context,
        message: String?
    ) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .background(chatZaBrown)
                .fillMaxSize()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(70.dp))

            //Header Text
            Text(
                "Forgot your Password ?",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "Enter your email to receive\na password reset link",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Image(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(56.dp)),
                painter = painterResource(id = R.drawable.email_veri_modified),
                contentDescription = "icon"
            )
            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                singleLine = true,
                value = forgotPasswordEmail,
                onValueChange = {
                    isEmailError = false
                    forgotPasswordEmail = it
                },
                label = { Text(text = "Email", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = chatZaBrown,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray
                ),
                isError = isEmailError
            )
            if (isEmailError) {
                androidx.compose.material.Text(
                    text = "Email is Invalid",
                    color = Color.Red,
                    style = androidx.compose.material.MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            //Send
            Button(
                onClick = {
                    validateEmail()
                    if (!isEmailError) {
                        //Forgot Password Stuff
                        authVM.sendPasswordResetEmail(forgotPasswordEmail)
                    }
                    forgotPasswordEmail = ""
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)
                    .height(50.dp)
                    .border(
                        border = BorderStroke(width = 0.75.dp, color = Color.DarkGray),
                        shape = RoundedCornerShape(50.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = chatZaBrown)
            ) {
                Text(text = "Send", fontSize = 15.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    when (val sendPasswordResetEmailResponse = authVM.sendPasswordResetEmailResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            val isPasswordResetEmailSent = sendPasswordResetEmailResponse.data
            LaunchedEffect(isPasswordResetEmailSent) {
                if (isPasswordResetEmailSent == true) {
                    navController.popBackStack()
                    navController.navigate(route = Screen.LoginPage.route)
                    showMessage(
                        context,
                        "We've sent you an email with a link to reset the password."
                    )
                }
            }
        }

        is Response.Failure -> {
            sendPasswordResetEmailResponse.apply {
                LaunchedEffect(e) {
                    print(e)
                    showMessage(context, e.message)
                }
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun ForgotPasswordPreview() {
    //ForgotPasswordScreen(navController = rememberNavController())
}
package com.mil.chatza.presentation.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.Start
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.R
import com.mil.chatza.domain.model.SuccessLogin
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, authVM: AuthViewModel) {

    val currentContext = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var progressBarState by remember { mutableStateOf(false) }

    fun validateLoginDetails(): Boolean {
        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordError = password.isEmpty()
        return isEmailError && isPasswordError
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(chatZaBrown, Color.White, chatZaBrown),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = SpaceBetween,
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    "Already\nhave an\n account?",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(56.dp))
                        .clickable { },
                    painter = painterResource(id = R.drawable.people), contentDescription = "icon"
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            //Username
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                singleLine = true,
                value = email,
                onValueChange = {
                    isEmailError = false
                    email = it
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
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Password
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                singleLine = true,
                label = { Text(text = "Password", color = Color.Gray) },
                value = password,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    isPasswordError = false
                    password = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = chatZaBrown,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray
                ),
                isError = isPasswordError,
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.password_eye),
                            contentDescription = null,
                            tint = if (passwordVisibility) Color(0xC4FFFFFF) else Color.DarkGray,
                        )
                    }
                },
            )
            if (isPasswordError) {
                androidx.compose.material.Text(
                    text = "Password can't be Empty",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            //Login
            Button(
                onClick = {
                    navController.navigate(Screen.DisclaimerPage.route)
                    /*validateLoginDetails()
                    if (!isEmailError && !isPasswordError) {
                        progressBarState = true
                        scope.launch {
                            //Login Success
                            progressBarState = if (authVM.logIn(email = email.trimEnd(), password = password.trimEnd()) == SuccessLogin(true)){
                                if (!authVM.auth.currentUser!!.isEmailVerified){
                                    Toast.makeText(currentContext, "Login Successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.VerifyEmailPage.route)
                                } else {
                                    //Go to create Profile
                                    Toast.makeText(currentContext, "Logged In and Verified", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.CreateProfilePage.route)
                                }
                                false
                            }else{
                                //Login Failure
                                email = ""
                                password = ""
                                Toast.makeText(currentContext, authVM.loginException.value!!.message.toString(), Toast.LENGTH_SHORT).show()
                                false
                            }
                        }
                    }*/
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
                Text(text = "Login", fontSize = 15.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(27.5.dp))
            Row {
                Text(
                    text = "New User?",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Default,
                        color = Color.DarkGray
                    )
                )
                Text(
                    modifier = Modifier.clickable { navController.navigate(Screen.RegisterPage.route) },
                    text = " Register Now",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Default,
                        color = chatZaBlue
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = SpaceBetween
            ) {
                listOf("Google", "Facebook").forEach {
                    Card(
                        modifier = Modifier
                            .background(Color.White),
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(0.5.dp, Color.DarkGray)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .wrapContentWidth()
                                .background(chatZaBrown)
                                .clickable {
                                    when (it) {
                                        "Google" -> {}
                                        "Facebook" -> {}
                                    }
                                },
                            horizontalArrangement = Start
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(
                                        start = 10.dp,
                                        top = 10.dp,
                                        bottom = 10.dp,
                                    ),
                                painter = painterResource(id = if (it == "Google") R.drawable.google_icon_png_rwscww else R.drawable.facebook_icon_9imsd),
                                contentDescription = it,
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                it,
                                modifier = Modifier.padding(end = 20.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
        when (progressBarState) {
            true -> ProgressBar()
            else -> {}
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewLogin() {
    LoginScreen(navController = rememberNavController(), authVM = AuthViewModel())
}
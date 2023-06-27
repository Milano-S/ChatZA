package com.mil.chatza.presentation.screens

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
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
import com.mil.chatza.domain.model.Success
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.launch


private const val TAG = "RegisterScreen"

@Composable
fun RegisterScreen(navController: NavHostController, authVM: AuthViewModel) {

    val currentContext = LocalContext.current
    val scope = rememberCoroutineScope()

    //Variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isPasswordLengthError by remember { mutableStateOf(false) }
    var isRepeatPasswordError by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(false) }
    var isTextShake by remember { mutableStateOf(false) }
    var progressBarState by remember { mutableStateOf(false) }

    fun validateRegisterDetails(): Boolean {
        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordError = password.isEmpty()
        isPasswordLengthError = password.length < 6
        return isEmailError && isPasswordError && isPasswordLengthError
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
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
                .fillMaxSize()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(200.dp))

            //Header Text
            Text(
                "Register",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            //Username
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                value = email,
                singleLine = true,
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
                label = { Text(text = "Password", color = Color.Gray) },
                value = password,
                singleLine = true,
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
                        Icon(
                            painter = painterResource(id = R.drawable.password_eye),
                            contentDescription = null,
                            tint = if (passwordVisibility) Color(0xC4FFFFFF) else Color.DarkGray,
                        )
                    }
                },
            )
            if (isPasswordError) {
                androidx.compose.material.Text(
                    text = if(isPasswordError)"Password can't be Empty" else "Password must be at least 6 characters",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Repeat Password
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                label = { Text(text = "Repeat Password", color = Color.Gray) },
                value = repeatPassword,
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = {
                    if (repeatPassword == password) {
                        isRepeatPasswordError = false
                    }
                    repeatPassword = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = chatZaBrown,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray
                ),
                isError = isRepeatPasswordError,
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = painterResource(id = R.drawable.password_eye),
                            contentDescription = null,
                            tint = if (passwordVisibility) Color(0xC4FFFFFF) else Color.DarkGray,
                        )
                    }
                },
            )
            if (isRepeatPasswordError) {
                androidx.compose.material.Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //T&Cs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //Checkbox
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        isTextShake = true
                        checkedState = it
                    },
                    modifier = Modifier
                        .size(80.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = chatZaBrown,
                        checkmarkColor = Color.White
                    )
                )

                Column() {
                    Text(
                        text = "I agree to ChatZa's ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Terms and Conditions ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = chatZaBlue,
                        modifier = Modifier
                            .clickable(onClick = {

                            })
                    )
                }
            }

            //Register
            Button(
                onClick = {
                    validateRegisterDetails()
                    isRepeatPasswordError = repeatPassword != password
                    if (!isEmailError && !isPasswordError && !isRepeatPasswordError) {
                        if (!checkedState) {
                            Toast.makeText(
                                currentContext,
                                "Please Accept the Terms and Conditions",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            //Sign Up
                            progressBarState = true
                            scope.launch {
                                //Progress Bar Visibility
                                progressBarState = if (
                                    authVM.signUp(
                                        email = email,
                                        password = password
                                    ) == Success(true)
                                ) {
                                    //Sign Up Success
                                    Toast.makeText(currentContext, "Account Successfully Created", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.VerifyEmailPage.route)
                                    log(authVM.signUp(email = email.trimEnd(), password = password.trimEnd()).toString())
                                    false
                                } else {
                                    //Sign Up Failure
                                    Toast.makeText(currentContext, authVM.signUpException.value?.message.toString(), Toast.LENGTH_SHORT).show()
                                    email = ""
                                    password = ""
                                    repeatPassword = ""
                                    log(authVM.signUpException.value?.message.toString())
                                    false
                                }
                            }
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
                Text(text = "Create Account", fontSize = 15.sp, color = Color.DarkGray)
            }
        }
        when (progressBarState) {
            true -> ProgressBar()
            else -> {}
        }
    }
}

private fun log(message: String) {
    Log.i(TAG, message)
}

fun Modifier.shake(enabled: Boolean, onAnimationFinish: () -> Unit) = composed(
    factory = {
        val distance by animateFloatAsState(
            targetValue = if (enabled) 15f else 0f,
            animationSpec = repeatable(
                iterations = 8,
                animation = tween(durationMillis = 50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            finishedListener = { onAnimationFinish.invoke() }
        )
        Modifier.graphicsLayer {
            translationX = if (enabled) distance else 0f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)


@Preview(showBackground = true)
@Composable
private fun PreviewRegister() {
    RegisterScreen(navController = rememberNavController(), authVM = AuthViewModel())
}

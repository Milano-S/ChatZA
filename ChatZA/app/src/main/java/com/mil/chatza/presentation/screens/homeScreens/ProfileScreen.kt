package com.mil.chatza.presentation.screens.homeScreens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mil.chatza.R
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.model.UserProfile
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.runBlocking


private const val TAG = "ProfileScreen"
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    authVM: AuthViewModel,
    firebaseVM: FirebaseViewModel
) {

    val currentContext = LocalContext.current
    val scope = rememberCoroutineScope()

    var progressBarState by remember { mutableStateOf(true) }

    suspend fun getUserDetails(): UserProfile {
        progressBarState = false
        return firebaseVM.getProfileDetails(authVM.auth.currentUser!!.email.toString())
    }

    val currentUserProfile = runBlocking {
        getUserDetails()
    }

    //Variables
    var username by remember { mutableStateOf(currentUserProfile.name) }
    var age by remember { mutableStateOf(currentUserProfile.age) }
    var isUsernameError by remember { mutableStateOf(false) }
    var isAgeError by remember { mutableStateOf(false) }
    var isProvinceError by remember { mutableStateOf(false) }
    var isGenderError by remember { mutableStateOf(false) }
    var genderFilterTerm by remember { mutableStateOf(currentUserProfile.gender) }
    var expanded by remember { mutableStateOf(false) }
    var selectedProvince by remember { mutableStateOf(currentUserProfile.province) }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedImageUri = it }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(30.dp))

            //Header Text
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(chatZaBlue, RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Profile",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            //Profile Picture
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = CircleShape,
            ) {

                AsyncImage(
                    model = selectedImageUri,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_2),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            try {
                                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            } catch (e: Exception) {
                                Log.i(TAG, e.message.toString())
                                Toast
                                    .makeText(
                                        currentContext,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    fallback = painterResource(id = R.drawable.profile_2)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            //Username
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                value = username,
                singleLine = true,
                onValueChange = {
                    isUsernameError = false
                    username = it
                },
                label = { Text(text = "Username", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = chatZaBrown,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    disabledTextColor = Color.Black
                ),
                isError = isUsernameError,
                enabled = false
            )
            if (isUsernameError) {
                androidx.compose.material.Text(
                    text = "Username can't be empty",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Age
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp
                    ),
                value = age,
                singleLine = true,
                onValueChange = { age = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text(text = "Age", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = chatZaBrown,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.DarkGray,
                    disabledTextColor = Color.Black
                ),
                isError = isAgeError,
                enabled = false
            )
            if (isAgeError) {
                androidx.compose.material.Text(
                    text = "This app is not recommended for minors",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { /*expanded = !expanded*/ }
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = selectedProvince,
                    onValueChange = { },
                    label = { Text("Province", color = Color.DarkGray) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        backgroundColor = chatZaBrown,
                        cursorColor = Color.DarkGray,
                        focusedIndicatorColor = Color.DarkGray,
                        focusedTrailingIconColor = chatZaBlue
                    )
                )
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(chatZaBrown)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Consts.provinceList.forEach { province ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .padding(
                                    horizontal = 20.dp
                                )
                                .fillMaxWidth(),
                            onClick = {
                                /*isProvinceError = false
                                selectedProvince = province
                                expanded = false*/
                            }
                        ) {
                            Text(text = province)
                        }
                    }
                }
            }
            if (isProvinceError) {
                androidx.compose.material.Text(
                    text = "Please select a province",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Switch Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        /*top = 10.dp,
                        bottom = 20.dp,*/
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Consts.genderList.forEach { gender ->
                    Chip(
                        border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                        shape = RoundedCornerShape(10.dp),
                        colors = ChipDefaults.chipColors(
                            backgroundColor = if (genderFilterTerm == gender) chatZaBlue
                            else chatZaBrown,
                            disabledBackgroundColor = Color.Gray,
                            disabledContentColor = Color.Black,
                            contentColor = Color.Black,
                        ),
                        onClick = {
                            /*isGenderError = false
                            genderFilterTerm = gender*/
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                    ) {
                        Text(
                            text = gender,
                            color = if (genderFilterTerm == gender) chatZaBrown else Color.DarkGray,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(9.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            if (isGenderError) {
                androidx.compose.material.Text(
                    text = "Please select a gender",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

        }
        when (progressBarState) {
            true -> ProgressBar()
            else -> {}
        }
    }
}

/*
@Composable
fun com.mil.chatza.presentation.screens.homeScreens.ProfileScreen() {

    val currentContext = LocalContext.current

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { selectedImageUri = it }
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(chatZaBrown)
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 15.dp,
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            //Top Bar
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(chatZaBlue, RoundedCornerShape(20.dp))
                    .border(
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(20.dp)
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Profile",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            //Profile Picture
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = CircleShape,
            ) {

                AsyncImage(
                    model = selectedImageUri,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.profile_2),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            try {
                                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            } catch (e: Exception) {
                                Log.i(com.mil.chatza.presentation.screens.homeScreens.TAG, e.message.toString())
                                Toast
                                    .makeText(
                                        currentContext,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    fallback = painterResource(id = R.drawable.profile_2)
                )
            }

            Text(
                modifier = Modifier.padding(10.dp),
                text = "Profile",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )
        }
    }

}
*/

@Preview
@Composable
private fun PreviewProfileScreen() {

}

package com.mil.chatza.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.colors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mil.chatza.R
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.model.SuccessUserUpload
import com.mil.chatza.domain.model.UserProfile
import com.mil.chatza.domain.repository.UserProfileRepositoryImp.Companion.age
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun DisclaimerScreen(
    navController: NavHostController,
) {

    val currentContext = LocalContext.current
    val scope = rememberCoroutineScope()

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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(70.dp))

            //Header Text
            Text(
                "Disclaimer",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Image(
                modifier = Modifier
                    .size(230.dp)
                    .clip(RoundedCornerShape(56.dp))
                    .clickable { },
                painter = painterResource(id = R.drawable.caution), contentDescription = "icon"
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                text = "Remember to be respectful to everyone.\n\n Stay safe and do not share your personal details with strangers",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            //Continue
            Button(
                onClick = {
                    navController.navigate(route = Consts.Companion.Graph.MAIN)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 45.dp)
                    .height(50.dp)
                    .border(
                        border = BorderStroke(width = 0.75.dp, color = Color.DarkGray),
                        shape = RoundedCornerShape(
                            50.dp
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = chatZaBrown)
            ) {
                Text(text = "Continue", fontSize = 15.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(56.dp))
                    .clickable { },
                painter = painterResource(id = R.drawable.people), contentDescription = "icon"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRegister() {
    DisclaimerScreen(
        navController = rememberNavController(),
    )
}


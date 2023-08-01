package com.mil.chatza.presentation.screens.homeScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mil.chatza.R
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.repository.UserProfileRepositoryImp.Companion.province
import com.mil.chatza.presentation.components.ProvinceChatCard
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authVM: AuthViewModel
) {

    val currentContext = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "ChatZA Groups")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                    ) {
                        Icon(
                            Icons.Rounded.Menu,
                            contentDescription = ""
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = chatZaBrown
                )
            )
        },
        drawerContent = { DrawerView(currentContext, scaffoldState, scope, authVM, navController) },
    ) { paddingValues ->
        print(paddingValues)
        HomePageContent()
    }
}

@Composable
private fun HomePageContent() {
    val currentContext = LocalContext.current
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
            ) {
                Consts.provinceList.forEach { province ->
                    item {
                        ProvinceChatCard(text = province, onClick = {
                            Toast.makeText(currentContext, province, Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerView(
    context: Context,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    authVM: AuthViewModel,
    navController: NavHostController
) {
    val language = listOf("Help", "Rate Us", "Log Out")
    LazyColumn {
        item { AddDrawerHeader() }
        items(language.size) { index ->

            AddDrawerContentView(
                title = language[index],
                menuItemClick = {
                    scope.launch { scaffoldState.drawerState.close() }
                    when (language[index]) {
                        "Log Out" -> {
                            authVM.signOut()
                            navController.navigate(Screen.LoginPage.route)
                            Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                        }

                        "Help" -> {
                            navController.navigate(Screen.HelpScreen.route)
                        }

                        else -> {
                            Toast.makeText(context, language[index], Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AddDrawerContentView(title: String, menuItemClick: () -> Unit) {
    val drawerIcon = when (title) {
        "Log Out" -> Icons.Default.ExitToApp
        "Rate Us" -> Icons.Default.Star
        else -> {
            Icons.Default.Info
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                menuItemClick.invoke()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Icons.Default.Settings
        Icon(drawerIcon, contentDescription = null, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 14.sp)
    }
}

@Composable
private fun AddDrawerHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray),
        border = BorderStroke(1.dp, color = Color.Black),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp)
                    .background(Color.DarkGray)
            ) {
                Image(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(RoundedCornerShape(56.dp))
                        .clickable { },
                    painter = painterResource(id = R.drawable.people), contentDescription = "icon"
                )
            }
        }
    }
}
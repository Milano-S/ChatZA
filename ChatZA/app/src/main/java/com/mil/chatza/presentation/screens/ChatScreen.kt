package com.mil.chatza.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.model.Chat
import com.mil.chatza.domain.model.Message
import com.mil.chatza.presentation.components.ChatMessageBubble
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.navigation.Screen
import com.mil.chatza.presentation.viewmodels.AuthViewModel
import com.mil.chatza.presentation.viewmodels.ChatZaViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "ChatScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatZaVM: ChatZaViewModel,
    authVM: AuthViewModel,
    firebaseVM: FirebaseViewModel
) {

    val currentContext = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val titleText by remember { mutableStateOf(chatZaVM.currentChatName.value.toString()) }
    var showDropDown by remember { mutableStateOf(false) }
    val dropDownMenuItems = listOf(
        Pair("Participants", Icons.Default.Person),
        Pair("Delete", Icons.Default.Delete),
    )
    //Adaptive Chunk Values
    val desiredColumnWidthDp = 160.dp
    val configuration = LocalConfiguration.current
    val screenWidthDp = with(LocalDensity.current) { configuration.screenWidthDp.dp }
    val columns = (screenWidthDp / desiredColumnWidthDp).toInt().coerceAtLeast(1)

    val scrollState = rememberLazyListState()
    var progressBarState by remember { mutableStateOf(true) }

    var chatDetails by remember { mutableStateOf<Chat?>(null) }
    var isChatJoined by remember { mutableStateOf<Boolean?>(null) }
    var messageText by remember { mutableStateOf("") }
    var launchKey by remember { mutableStateOf(0) }
    LaunchedEffect(launchKey) {
        var lastMessageCount = 0
        scope.launch {
            while (true) {
                try {
                    chatDetails =
                        firebaseVM.getChatDetails(chatZaVM.currentChatName.value.toString())
                    isChatJoined =
                        firebaseVM.currentProfileDetails.value!!.chatGroups.contains(chatDetails!!.chatName)
                    if (chatDetails!!.messages.size > lastMessageCount) {
                        try {
                            scrollState.animateScrollToItem(chatDetails!!.messages.size - 1)
                            lastMessageCount = chatDetails!!.messages.size
                        } catch (e: Exception) {
                            Log.i(TAG, e.message.toString())
                        }
                    }
                } catch (e: Exception) {
                    Log.i(TAG, e.message.toString())
                    Toast.makeText(currentContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
                delay(1000)
            }
        }
    }
    progressBarState = false

    BackHandler(enabled = true) {
        when (navController.previousBackStackEntry?.destination?.route) {
            Screen.ProfileDetailsScreen.route -> {
                navController.navigate(Consts.Companion.Graph.MAIN)
            }

            else -> {
                navController.popBackStack()
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text =
                        titleText
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (!Consts.provinceList.contains(titleText)) {
                        IconButton(onClick = {
                            showDropDown = !showDropDown
                        }) { Icon(Icons.Filled.MoreVert, contentDescription = null) }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = chatZaBrown
                )
            )
        },
    ) { paddingValues ->
        print(paddingValues)

        Surface(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                /*.verticalScroll(rememberScrollState())*/,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                //Drop Down
                DropdownMenu(
                    expanded = showDropDown,
                    onDismissRequest = { showDropDown = false },
                    modifier = Modifier
                        .background(color = White)
                        .border(width = 1.dp, color = DarkGray)
                ) {
                    dropDownMenuItems.forEach { (text, icon) ->
                        DropdownMenuItem(
                            colors = MenuDefaults.itemColors(leadingIconColor = DarkGray),
                            text = { Text(text = text, color = DarkGray) },
                            onClick = { },
                            leadingIcon = {
                                androidx.compose.material3.Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = DarkGray,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        )
                    }
                }

                LazyColumn(
                    state = scrollState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    // Messages
                    if (chatDetails != null) {
                        items(chatDetails!!.messages.chunked(columns)) { messageList ->
                            messageList.forEach { message ->
                                val previousMessage: Message? =
                                    if (messageList.indexOf(message) != 0) messageList[messageList.indexOf(
                                        message
                                    ) - 1] else null
                                ChatMessageBubble(
                                    message = message,
                                    isUser = message.sender.email == authVM.auth.currentUser!!.email,
                                    isPreviousMessage = false,
                                    isFriendChat = chatDetails!!.isFriendChat,
                                    userName = message.sender.name,
                                    profileImageUrl = message.sender.profileImageUrl,
                                    firebaseVM = firebaseVM,
                                    profileClick = {
                                        chatZaVM.setCurrentUserDetails(message.sender)
                                        navController.navigate(Screen.ProfileDetailsScreen.route)
                                    }
                                )
                            }
                        }
                    }

                    /*item {
                        Column(modifier = Modifier.fillMaxSize()) {
                            repeat(50) {
                                chatDetails?.messages?.forEach { message ->
                                    ChatMessageBubble(
                                        message = message,
                                        isUser = message.sender.email == firebaseVM.currentProfileDetails.value?.email,
                                        userName = message.sender.name
                                    )
                                }
                            }
                        }
                    }*/

                    item { Spacer(modifier = Modifier.weight(1f)) }

                    item {
                        //Join
                        Row {
                            when (isChatJoined) {
                                false -> {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                progressBarState = true
                                                try {
                                                    firebaseVM.joinChatGroup(
                                                        chatDetails = chatDetails!!,
                                                        userDetails = firebaseVM.currentProfileDetails.value
                                                    )
                                                    isChatJoined = true
                                                } catch (e: Exception) {
                                                    Log.i(TAG, e.message.toString())
                                                    Toast.makeText(
                                                        currentContext,
                                                        e.message.toString(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                progressBarState = false
                                            }
                                        },
                                        shape = RoundedCornerShape(50.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 45.dp, vertical = 20.dp)
                                            .height(50.dp)
                                            .border(
                                                border = BorderStroke(
                                                    width = 0.5.dp,
                                                    color = Color.DarkGray
                                                ),
                                                shape = RoundedCornerShape(50.dp)
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = chatZaBlue)
                                    ) {
                                        androidx.compose.material3.Text(
                                            text = "Join",
                                            fontSize = 15.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }

                                true -> {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .fillMaxWidth(),
                                        value = messageText,
                                        onValueChange = { messageText = it },
                                        label = {
                                            Text(
                                                "Message...",
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = chatZaBlue,
                                            unfocusedBorderColor = chatZaBlue,
                                        ),
                                        trailingIcon = {
                                            Icon(
                                                modifier = Modifier.clickable {
                                                    if (messageText.isNotBlank()) {
                                                        scope.launch {
                                                            try {
                                                                firebaseVM.sendMessage(
                                                                    chatDetails = chatDetails!!,
                                                                    newMessage = Message(
                                                                        sender = firebaseVM.currentProfileDetails.value!!,
                                                                        message = messageText
                                                                    )
                                                                )
                                                                messageText = ""
                                                            } catch (e: Exception) {
                                                                messageText = ""
                                                                Log.i(TAG, e.message.toString())
                                                                Toast.makeText(
                                                                    currentContext,
                                                                    e.message.toString(),
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                            launchKey++
                                                        }
                                                    }
                                                },
                                                imageVector = Icons.Default.Send,
                                                contentDescription = null
                                            )
                                        },
                                    )
                                }

                                else -> {}
                            }
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


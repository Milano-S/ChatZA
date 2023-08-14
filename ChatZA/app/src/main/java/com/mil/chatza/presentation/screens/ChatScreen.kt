package com.mil.chatza.presentation.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.play.integrity.internal.c
import com.google.android.play.integrity.internal.f
import com.mil.chatza.domain.model.Chat
import com.mil.chatza.domain.model.Message
import com.mil.chatza.presentation.components.ChatMessageBubble
import com.mil.chatza.presentation.components.ProgressBar
import com.mil.chatza.presentation.viewmodels.ChatZaViewModel
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import kotlinx.coroutines.launch


private const val TAG = "ChatScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatZaVM: ChatZaViewModel,
    firebaseVM: FirebaseViewModel
) {

    val currentContext = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(text = chatZaVM.currentChatName.value.toString())
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = chatZaBrown
                )
            )
        },
    ) { paddingValues ->
        print(paddingValues)
        ChatScreenContent(firebaseVM = firebaseVM, chatZaVM = chatZaVM)
    }
}

@Composable
private fun ChatScreenContent(
    chatZaVM: ChatZaViewModel,
    firebaseVM: FirebaseViewModel
) {
    val scope = rememberCoroutineScope()
    val currentContext = LocalContext.current

    var progressBarState by remember { mutableStateOf(true) }

    var chatDetails by remember { mutableStateOf<Chat?>(null) }
    var isChatJoined by remember { mutableStateOf<Boolean?>(null) }
    var messageText by remember { mutableStateOf("") }
    var launchKey by remember { mutableStateOf(0) }

    LaunchedEffect(launchKey) {
        try {
            progressBarState = true
            chatDetails = firebaseVM.getChatDetails(chatZaVM.currentChatName.value.toString())
            isChatJoined =
                chatDetails!!.participants.contains(firebaseVM.currentProfileDetails.value)
            progressBarState = false
        } catch (e: Exception) {
            progressBarState = false
            Log.i(TAG, e.message.toString())
            Toast.makeText(currentContext, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            //Messages
            Column(modifier = Modifier.fillMaxSize()) {
                chatDetails?.messages?.forEach { message ->
                    ChatMessageBubble(
                        message = message,
                        isUser = message.sender.email == firebaseVM.currentProfileDetails.value?.email,
                        userName = message.sender.name
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
                                    border = BorderStroke(width = 0.5.dp, color = Color.DarkGray),
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
                            label = { Text("Message...", color = Color.Gray, fontSize = 16.sp) },
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
        when (progressBarState) {
            true -> ProgressBar()
            else -> {}
        }
    }
}

@Composable
private fun OutlinedTextField(
    chatDetails: Chat,
    firebaseVM: FirebaseViewModel,
    chatZaVM: ChatZaViewModel
) {
    val scope = rememberCoroutineScope()
    val currentContext = LocalContext.current
    var messageText by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        value = messageText,
        onValueChange = { messageText = it },
        label = { Text("Message...", color = Color.Gray, fontSize = 16.sp) },
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
                                    chatDetails = chatDetails,
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
                        }
                    }
                },
                imageVector = Icons.Default.Send,
                contentDescription = "Clear"
            )
        },
    )
}

@Preview
@Composable
private fun PreviewChatScreen() {
    //ChatScreen(rememberNavController())
}
package com.mil.chatza.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mil.chatza.R
import com.mil.chatza.domain.model.Message
import com.mil.chatza.presentation.viewmodels.FirebaseViewModel
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrownDark

private const val TAG = "ChatMessageBubble"

@Composable
fun ChatMessageBubble(
    userName: String,
    message: Message,
    profileImageUrl: String,
    isUser: Boolean,
    isPreviousMessage: Boolean,
    firebaseVM: FirebaseViewModel,
    profileClick : () -> Unit
) {
    val backgroundColor = if (isUser) chatZaBlue else chatZaBrownDark
    val horizontalAlignment = if (isUser) Arrangement.End else Arrangement.Start
    val bubbleShape = RoundedCornerShape(7.dp)

    var profileUrl by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            profileUrl = firebaseVM.getDownloadUrlFromGsUrl(
                firebaseVM.replaceEncodedColon(profileImageUrl)
            )
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 2.5.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isPreviousMessage) {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Profile Picture
                Card(
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .clickable { profileClick.invoke() },
                    border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = CircleShape,
                ) {

                    AsyncImage(
                        model = profileUrl,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.profile_2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        fallback = painterResource(id = R.drawable.profile_2)
                    )
                }
                Text(
                    text = userName,
                    color = Color.DarkGray,
                    fontSize = 14.5.sp,
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 0.dp
                    )
                )
            }
        }
        Row(
            horizontalArrangement = horizontalAlignment
        ) {
            Surface(
                color = backgroundColor,
                shape = bubbleShape,
                elevation = 4.dp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Text(
                    text = message.message,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun ChatBubblePreview() {
    Column {
        /*ChatMessageBubble(
            message = Message(message = "Hello World!!"),
            isUser = false,
            userName = "User name"
        )*/
        //ChatMessageBubble(message = "Hi there!", isUser = true, userName = "User Name")
    }
}

package com.mil.chatza.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mil.chatza.domain.model.Message
import com.mil.chatza.ui.theme.chatZaBlue
import com.mil.chatza.ui.theme.chatZaBrown
import com.mil.chatza.ui.theme.chatZaBrownDark

@Composable
fun ChatMessageBubble(userName: String, message: Message, isUser: Boolean) {
    val backgroundColor = if (isUser) chatZaBlue else chatZaBrownDark
    val horizontalAlignment = if (isUser) Arrangement.End else Arrangement.Start
    val bubbleShape = RoundedCornerShape(8.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.5.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Text(
            text = userName,
            color = Color.DarkGray,
            fontSize = 13.sp,
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 2.dp,
                bottom = 0.dp
            )
        )
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
        //ChatMessageBubble(message = "Hello!", isUser = false, userName = "User name")
        //ChatMessageBubble(message = "Hi there!", isUser = true, userName = "User Name")
    }
}

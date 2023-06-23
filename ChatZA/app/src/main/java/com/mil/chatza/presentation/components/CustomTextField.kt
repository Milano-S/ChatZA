package com.mil.chatza.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: @Composable (() -> Unit)?,
    onChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyBoardActions: KeyboardActions = KeyboardActions(),
    isEnabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    showError: Boolean = false,
    errorMessage: String = "",
    textStyle: TextStyle = TextStyle(),
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: Shape = TextFieldDefaults.shape,
    unfocusedColor: Color = Color.Black,
    focusedColor: Color = Color.Black,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    androidx.compose.material3.OutlinedTextField(
        shape = shape,
        visualTransformation = visualTransformation,
        placeholder = placeholder,
        textStyle = textStyle,
        value = value,
        readOnly = readOnly,
        isError = showError,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        leadingIcon = leadingIcon,
        enabled = isEnabled,
        trailingIcon = trailingIcon,
        onValueChange = onChange,
        label = label,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedPlaceholderColor = Color.Black,
            unfocusedPlaceholderColor = Color.Black,
            unfocusedIndicatorColor = Color.Black,
            focusedLabelColor = Color.Black
        ),
//        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color.Black,
//            cursorColor = Color.Black,
//            focusedIndicatorColor = Color.Black,
//            backgroundColor = Color.White,
//            placeholderColor = Color.Black,
//            unfocusedLabelColor = Color.Black,
//            unfocusedIndicatorColor = Color.Black,
//            focusedLabelColor = TabalotGreen
//        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    )
    if (showError) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 25.dp)
                .offset(y = (-8).dp)
                .fillMaxWidth(0.9f)
        )
    }
}
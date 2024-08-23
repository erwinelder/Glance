package com.ataglance.walletglance.presentation.ui.uielements.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldWithLabel(
    text: String,
    placeholderText: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    fontSize: TextUnit = 22.sp,
    cornerSize: Dp = 15.dp,
    labelText: String = "Label"
) {
    FieldWithLabel(labelText) {
        GlanceTextField(
            text = text,
            placeholderText = placeholderText,
            onValueChange = onValueChange,
            isError = isError,
            keyboardType = keyboardType,
            fontSize = fontSize,
            cornerSize = cornerSize
        )
    }
}
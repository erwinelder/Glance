package com.ataglance.walletglance.ui.theme.uielements.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextFieldWithLabel(
    text: String,
    placeholderText: String = "",
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    fontSize: TextUnit = 22.sp,
    cornerSize: Dp = 15.dp,
    labelText: String = "Label",
    labelFontSize: TextUnit = 16.sp,
    gap: Dp = 4.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        FieldLabel(labelText, labelFontSize)
        CustomTextField(
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
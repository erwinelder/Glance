package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallTextFieldWithLabel(
    text: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    placeholderText: String = "",
    fontSize: TextUnit = 19.sp,
    cornerSize: Dp = 15.dp,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onDoneKeyboardAction: () -> Unit = {},
    onGoKeyboardAction: () -> Unit = {}
) {
    FieldWithLabelWrapper(labelText = labelText) {
        SmallTextField(
            text = text,
            onValueChange = onValueChange,
            placeholderText = placeholderText,
            fontSize = fontSize,
            cornerSize = cornerSize,
            isError = isError,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onDoneKeyboardAction = onDoneKeyboardAction,
            onGoKeyboardAction = onGoKeyboardAction
        )
    }
}
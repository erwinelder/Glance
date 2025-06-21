package com.ataglance.walletglance.errorHandling.presentation.component.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.component.field.SmallTextField
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState

@Composable
fun SmallTextFieldWithLabelAndMessages(
    state: ValidatedFieldState,
    onValueChange: (String) -> Unit,
    labelText: String? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 19.sp,
    cornerSize: Dp = 15.dp,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onDoneKeyboardAction: () -> Unit = {},
    onGoKeyboardAction: () -> Unit = {}
) {
    FieldWithLabelAndMessagesWrapper(state = state, labelText = labelText) {
        SmallTextField(
            text = state.fieldText,
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
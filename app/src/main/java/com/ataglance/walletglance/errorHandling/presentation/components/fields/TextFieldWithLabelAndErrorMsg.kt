package com.ataglance.walletglance.errorHandling.presentation.components.fields

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.components.fields.GlanceTextField
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState

@Composable
fun TextFieldWithLabelAndErrorMsg(
    state: ValidatedFieldUiState,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    fontSize: TextUnit = 20.sp,
    cornerSize: Dp = 15.dp,
    labelText: String = "Label",
) {
    FieldWithLabelAndMessages(labelText = labelText, state = state) {
        GlanceTextField(
            text = state.fieldText,
            placeholderText = placeholderText,
            onValueChange = onValueChange,
            isError = isError,
            keyboardType = keyboardType,
            fontSize = fontSize,
            cornerSize = cornerSize
        )
    }
}
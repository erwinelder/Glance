package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun SmallTextField(
    text: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    textColor: Color = GlanceColors.onSurface,
    fontSize: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.W400,
    cornerSize: Dp = 15.dp,
    readOnly: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onDoneKeyboardAction: () -> Unit = {},
    onGoKeyboardAction: () -> Unit = {},
    glassSurfaceModifier: Modifier = Modifier
) {
    CustomBasicTextFieldGlassSurface(
        text = text,
        onValueChange = onValueChange,
        placeholderText = placeholderText,
        textColor = textColor,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        contentAlignment = Alignment.Center,
        padding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        cornerSize = cornerSize,
        readOnly = readOnly,
        isError = isError,
        keyboardType = keyboardType,
        imeAction = imeAction,
        onDoneKeyboardAction = onDoneKeyboardAction,
        onGoKeyboardAction = onGoKeyboardAction,
        maxLines = 1,
        glassSurfaceModifier = glassSurfaceModifier,
        filledWidthByScreenType = null,
        textFieldModifier = Modifier.animateContentSize(),
        placeholderModifier = Modifier.run { if (text.isEmpty()) width(123.dp) else this }
    )
}
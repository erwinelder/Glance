package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.layout.PaddingValues
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
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface

@Composable
fun CustomBasicTextFieldGlassSurface(
    text: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    textColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    textAlign: TextAlign,
    contentAlignment: Alignment,
    padding: PaddingValues,
    cornerSize: Dp,
    readOnly: Boolean,
    isError: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onDoneKeyboardAction: () -> Unit = {},
    onGoKeyboardAction: () -> Unit = {},
    maxLines: Int,
    glassSurfaceModifier: Modifier = Modifier,
    filledWidthByScreenType: FilledWidthByScreenType? = FilledWidthByScreenType(),
    textFieldModifier: Modifier = Modifier,
    placeholderModifier: Modifier = Modifier
) {
    GlassSurface(
        filledWidths = filledWidthByScreenType,
        cornerSize = cornerSize,
        modifier = glassSurfaceModifier
    ) {
        CustomBasicTextField(
            text = text,
            onValueChange = onValueChange,
            placeholderText = placeholderText,
            textColor = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            contentAlignment = contentAlignment,
            padding = padding,
            cornerSize = cornerSize,
            readOnly = readOnly,
            isError = isError,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onDoneKeyboardAction = onDoneKeyboardAction,
            onGoKeyboardAction = onGoKeyboardAction,
            maxLines = maxLines,
            textFieldModifier = textFieldModifier,
            placeholderModifier = placeholderModifier
        )
    }
}
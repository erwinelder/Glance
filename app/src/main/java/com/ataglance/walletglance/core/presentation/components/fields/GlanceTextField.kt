package com.ataglance.walletglance.core.presentation.components.fields

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlanceTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    readOnly: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    fontSize: TextUnit = 22.sp,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    cornerSize: Dp = 15.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textColor = GlanceTheme.onSurface
    val containerColor = GlanceTheme.surface

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        readOnly = readOnly,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        textStyle = TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = Manrope,
            textAlign = TextAlign.Center
        ),
        modifier = modifier.animateContentSize()
    ) {
        TextFieldDefaults.DecorationBox(
            value = text,
            singleLine = true,
            enabled = true,
            isError = isError,
            interactionSource = interactionSource,
            visualTransformation = VisualTransformation.None,
            shape = RoundedCornerShape(cornerSize),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                errorContainerColor = GlanceTheme.errorContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            contentPadding = padding,
            innerTextField = {
                Box(contentAlignment = Alignment.Center) {
                    if (text.isNotBlank()) {
                        it()
                    } else {
                        Text(
                            text = placeholderText,
                            fontSize = fontSize,
                            color = GlanceTheme.outline,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Manrope,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}
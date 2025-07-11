package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.utils.end
import com.ataglance.walletglance.core.presentation.utils.start
import com.ataglance.walletglance.core.presentation.utils.top

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBasicTextField(
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
    textFieldModifier: Modifier = Modifier.Companion,
    placeholderModifier: Modifier = Modifier.Companion
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    var isPasswordVisible by remember {
        mutableStateOf(false.takeIf {
            keyboardType == KeyboardType.Companion.Password ||
                    keyboardType == KeyboardType.Companion.NumberPassword
        })
    }
    val visualTransformation = if (isPasswordVisible == false) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.Companion.None
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = padding.start, end = padding.end)
    ) {
        TextSelectionColorsProviderWrapper {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                readOnly = readOnly,
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Companion.Down) },
                    onDone = {
                        focusManager.clearFocus()
                        onDoneKeyboardAction()
                    },
                    onGo = {
                        focusManager.clearFocus()
                        onGoKeyboardAction()
                    }
                ),
                maxLines = maxLines,
                visualTransformation = visualTransformation,
                singleLine = true,
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    fontFamily = Manrope,
                    textAlign = textAlign
                ),
                cursorBrush = Brush.linearGradient(GlanciColors.primaryGlassGradient),
                modifier = textFieldModifier
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
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        cursorColor = GlanciColors.primary,
                        unfocusedTrailingIconColor = GlanciColors.primary,
                        focusedTrailingIconColor = GlanciColors.primary,
                        disabledTrailingIconColor = GlanciColors.primary,
                        errorTrailingIconColor = GlanciColors.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(top = padding.top, bottom = padding.bottom),
                    innerTextField = {
                        Box(contentAlignment = contentAlignment) {
                            it()
                            if (text.isBlank()) {
                                Placeholder(
                                    text = placeholderText,
                                    fontSize = fontSize,
                                    textAlign = textAlign,
                                    maxLines = maxLines,
                                    modifier = placeholderModifier
                                )
                            }
                        }
                    }
                )
            }
        }
        isPasswordVisible?.let {
            ShowHidePasswordIcon(isPasswordVisible = it) { isPasswordVisible = !it }
        }
    }
}

@Composable
private fun Placeholder(
    text: String,
    fontSize: TextUnit,
    textAlign: TextAlign,
    maxLines: Int,
    modifier: Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = GlanciColors.outline,
        fontWeight = FontWeight.Companion.Normal,
        textAlign = textAlign,
        fontFamily = Manrope,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
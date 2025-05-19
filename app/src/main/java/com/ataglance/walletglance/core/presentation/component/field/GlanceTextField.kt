package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.utils.takeComposableIfNotNull

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
    fontSize: TextUnit = 20.sp,
    padding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    cornerSize: Dp = 15.dp
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textColor = GlanceColors.onSurface
    val containerColor = GlanceColors.surface

    var isPasswordVisible by remember {
        mutableStateOf(false.takeIf {
            keyboardType == KeyboardType.Password ||
            keyboardType == KeyboardType.NumberPassword
        })
    }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        readOnly = readOnly,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = if (isPasswordVisible == false) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
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
                errorContainerColor = GlanceColors.error,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            contentPadding = padding,
            trailingIcon = takeComposableIfNotNull(nullableItem = isPasswordVisible) {
                IconButton(onClick = { isPasswordVisible = !it }) {
                    Icon(
                        painter = painterResource(
                            if (it) R.drawable.hide_icon else R.drawable.show_icon
                        ),
                        tint = GlanceColors.onSurface,
                        contentDescription = if (it) "Hide password" else "Show password",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            innerTextField = {
                Box(contentAlignment = Alignment.Center) {
                    if (text.isNotBlank()) {
                        it()
                    } else {
                        Text(
                            text = placeholderText,
                            fontSize = fontSize,
                            color = GlanceColors.outline,
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
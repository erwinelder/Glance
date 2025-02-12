package com.ataglance.walletglance.errorHandling.presentation.components.fields

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.errorHandling.presentation.model.ValidationUiState

@Composable
fun FieldMsg(
    state: ValidationUiState,
    fontSize: TextUnit = 16.sp
) {
    val context = LocalContext.current

    AnimatedContent(targetState = state) { targetState ->
        Text(
            text = "* " + context.getString(targetState.messageRes),
            fontSize = fontSize,
            color = if (targetState.isValid) GlanceColors.success else GlanceColors.error,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope,
            textAlign = TextAlign.Center
        )
    }
}
package com.ataglance.walletglance.core.presentation.components.fields

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.componentState.FieldValidationState
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope

@Composable
fun FieldMsg(
    state: FieldValidationState,
    fontSize: TextUnit = 16.sp
) {
    val context = LocalContext.current

    AnimatedContent(
        targetState = state,
        label = "field message"
    ) { targetState ->
        Text(
            text = "* " + context.getString(targetState.messageRes),
            fontSize = fontSize,
            color = if (targetState.isValid) GlanceTheme.success else GlanceTheme.error,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope,
            textAlign = TextAlign.Center
        )
    }
}
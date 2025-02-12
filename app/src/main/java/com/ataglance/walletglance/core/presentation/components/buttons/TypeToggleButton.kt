package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun TypeToggleButton(
    text: String,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GlanceColors.onPrimary,
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .bounceClickEffect(.97f)
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceColors.primaryGradient.reversed(),
                    start = Offset(75f, 210f),
                    end = Offset(95f, -10f)
                )
            )
            .border(1.dp, Color.Transparent, RoundedCornerShape(50))
    ) {
        AnimatedContent(targetState = text) { targetText ->
            Text(
                text = targetText,
                fontSize = fontSize,
                fontFamily = Manrope
            )
        }
    }
}
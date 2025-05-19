package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact

@Composable
fun SecondaryButton(
    text: String,
    enabled: Boolean = true,
    enabledGradientColor: Pair<Color, Color> = GlanceColors.glassGradientPair,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    val borderColor = if (enabled) GlanceColors.primaryGlassBorder else GlanceColors.outline
    val buttonLighterColor by animateColorAsState(
        targetValue = enabledGradientColor.second.copy(alpha = .5f)
    )
    val buttonDarkerColor by animateColorAsState(
        targetValue = enabledGradientColor.first.copy(alpha = .5f)
    )
    val modifier = if (WindowTypeIsCompact) {
        Modifier.fillMaxWidth(.84f)
    } else {
        Modifier.width(400.dp)
    }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = GlanceColors.surface.copy(.08f),
            contentColor = GlanceColors.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GlanceColors.outline
        ),
        shape = RoundedCornerShape(21.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = modifier
            .bounceClickEffect(.98f, enabled = enabled)
            .clip(RoundedCornerShape(21.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(buttonDarkerColor, buttonLighterColor),
                    start = Offset(75f, 200f),
                    end = Offset(100f, 0f)
                )
            )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = Manrope
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewSecondaryButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SecondaryButton(
            text = "Save and continue",
            onClick = {}
        )
    }
}
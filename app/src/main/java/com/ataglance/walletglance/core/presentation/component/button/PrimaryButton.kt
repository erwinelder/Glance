package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 17.sp,
    gradientColor: Pair<Color, Color> = GlanciColors.primaryGlassGradientPair,
    onClick: () -> Unit
) {
    val cornerSize = 21.dp
    val buttonLighterColor by animateColorAsState(
        targetValue = if (enabled) gradientColor.first else
            GlanciColors.disabledGlassGradientPair.first
    )
    val buttonDarkerColor by animateColorAsState(
        targetValue = if (enabled) gradientColor.second else
            GlanciColors.disabledGlassGradientPair.second
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.bounceClickEffect(.98f, enabled = enabled)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GlanciColors.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = GlanciColors.onPrimary,
            ),
            contentPadding = PaddingValues(vertical = 13.dp),
            modifier = Modifier
                .run {
                    if (WindowTypeIsCompact) fillMaxWidth(.84f) else width(400.dp)
                }
                .clip(RoundedCornerShape(cornerSize))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(buttonDarkerColor, buttonLighterColor),
                        start = Offset(75f, 210f),
                        end = Offset(95f, -10f)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = GlanciColors.primaryGlassBorderGradient,
                        start = Offset(10f, 0f),
                        end = Offset(0f, 100f)
                    ),
                    shape = RoundedCornerShape(cornerSize)
                )
                .padding(1.dp)
        ) {
            Text(
                text = text,
                fontSize = fontSize,
                fontFamily = Manrope
            )
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewPrimaryButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        PrimaryButton(text = "Save and continue") {}
    }
}
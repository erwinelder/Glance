package com.ataglance.walletglance.presentation.ui.uielements.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.Manrope
import com.ataglance.walletglance.presentation.ui.WindowTypeIsCompact
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.containers.PreviewContainer

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 18.sp,
    enabledGradientColor: Pair<Color, Color> = GlanceTheme.primaryGradientLightToDark,
    onClick: () -> Unit = {}
) {
    val buttonLighterColor by animateColorAsState(
        targetValue = if (enabled) enabledGradientColor.first else GlanceTheme.disabledGradientLightToDark.first,
        label = "PrimaryButton container color"
    )
    val buttonDarkerColor by animateColorAsState(
        targetValue = if (enabled) enabledGradientColor.second else GlanceTheme.disabledGradientLightToDark.second,
        label = "PrimaryButton container color"
    )
    val modifier = if (WindowTypeIsCompact) {
        Modifier.fillMaxWidth(.82f)
    } else {
        Modifier.width(400.dp)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.bounceClickEffect(.97f, enabled)
    ) {
        Shadow(enabled, buttonLighterColor)
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = GlanceTheme.onPrimary,
            ),
            contentPadding = PaddingValues(vertical = 18.dp),
            modifier = modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.large_button_corners)))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(buttonDarkerColor, buttonLighterColor),
                        start = Offset(75f, 210f),
                        end = Offset(95f, -10f)
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
}

@Composable
private fun Shadow(enabled: Boolean, enabledColor: Color) {
    val modifier = if (WindowTypeIsCompact) {
        Modifier.fillMaxWidth(.62f)
    } else {
        Modifier.width(300.dp)
    }
    val color by animateColorAsState(
        targetValue = if (enabled) {
            enabledColor
        } else {
            GlanceTheme.outline
        },
        label = "primary button shadow color"
    )

    Spacer(
        modifier = modifier
            .offset(y = -(5).dp)
            .height(20.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(dimensionResource(R.dimen.large_button_corners)),
                spotColor = color,
                ambientColor = Color.Transparent
            )
    )
}


@Preview(showSystemUi = true)
@Composable
private fun PreviewPrimaryButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        PrimaryButton(text = "Save and continue")
    }
}
package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun SmallPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    enabledGradient: Pair<Color, Color> = GlanceColors.primaryGradientPair,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    val lighterGradientColor by animateColorAsState(
        targetValue = if (enabled) enabledGradient.first else GlanceColors.disabledGradientPair.first,
        label = "small primary button gradient lighter color"
    )
    val darkerGradientColor by animateColorAsState(
        targetValue = if (enabled) enabledGradient.second else GlanceColors.disabledGradientPair.second,
        label = "small primary button gradient darker color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.bounceClickEffect(.96f, enabled)
    ) {
        Shadow(enabled, lighterGradientColor, fontSize, text)
        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceColors.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = GlanceColors.onPrimary
            ),
            contentPadding = PaddingValues(vertical = 15.dp, horizontal = 30.dp),
            modifier = modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.medium_button_corners)))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(darkerGradientColor, lighterGradientColor),
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
}

@Composable
private fun Shadow(enabled: Boolean, enabledColor: Color, buttonFontSize: TextUnit, text: String) {
    val color by animateColorAsState(
        targetValue = if (enabled) {
            enabledColor
        } else {
            Color.Transparent
        },
        label = "SmallPrimaryButton shadow color"
    )
    Text(
        text = text,
        fontSize = buttonFontSize,
        modifier = Modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(dimensionResource(R.dimen.medium_button_corners)),
                spotColor = color,
                ambientColor = Color.Transparent
            )
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewPrimarySmallButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallPrimaryButton(onClick = {}, text = "ApplyApplyApply")
    }
}
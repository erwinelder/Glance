package com.ataglance.walletglance.presentation.ui.uielements.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.Manrope
import com.ataglance.walletglance.presentation.ui.WalletGlanceTheme
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect

@Composable
fun SmallPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    enabledGradientColor: Pair<Color, Color> = GlanceTheme.primaryGradientLightToDark,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    val lighterGradientColor by animateColorAsState(
        targetValue = if (enabled) enabledGradientColor.first else GlanceTheme.disabledGradientLightToDark.first,
        label = "small primary button gradient lighter color"
    )
    val darkerGradientColor by animateColorAsState(
        targetValue = if (enabled) enabledGradientColor.second else GlanceTheme.disabledGradientLightToDark.second,
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
                contentColor = GlanceTheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = GlanceTheme.onPrimary
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
    BoxWithConstraints {
        WalletGlanceTheme(boxWithConstraintsScope = this) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                SmallPrimaryButton(onClick = {}, text = "ApplyApplyApply")
            }
        }
    }
}
package com.ataglance.walletglance.core.presentation.component.other

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun LargePrimaryIcon(
    @DrawableRes iconRes: Int,
    gradientColor: List<Color>,
    iconDescription: String,
    iconTint: Color = GlanciColors.onPrimary,
    iconSize: Dp = 100.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                spotColor = gradientColor.first()
            )
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColor,
                    start = Offset(240f, 0f),
                    end = Offset(0f, 240f)
                )
            )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun IconWithBackgroundLightDefaultPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        LargePrimaryIcon(
            iconRes = R.drawable.success_large_icon,
            gradientColor = GlanciColors.primaryGlassGradient,
            iconDescription = ""
        )
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun IconWithBackgroundDarkDefaultPreview() {

    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        LargePrimaryIcon(
            iconRes = R.drawable.success_large_icon,
            gradientColor = GlanciColors.primaryGlassGradient,
            iconDescription = ""
        )
    }
}
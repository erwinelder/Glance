package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.model.IconOrientation
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun SmallTertiaryButton(
    text: String,
    @DrawableRes iconRes: Int? = null,
    iconSize: DpSize = DpSize(16.dp, 16.dp),
    iconOrientation: IconOrientation = IconOrientation.Left,
    enabled: Boolean = true,
    gradientColor: Pair<Color, Color> = GlanciColors.glassGradientPair,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClickEffect(shrinkScale = .98f, enabled = enabled, onClick = onClick)
            .clip(RoundedCornerShape(42))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientColor.second, gradientColor.first),
                    start = Offset(75f, 200f),
                    end = Offset(100f, 0f)
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (iconOrientation == IconOrientation.Left) {
            IconComponent(iconRes = iconRes, iconSize = iconSize)
        }
        Text(
            text = text,
            color = GlanciColors.onSurface,
            fontSize = 17.sp,
            fontFamily = Manrope,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
        if (iconOrientation == IconOrientation.Right) {
            IconComponent(iconRes = iconRes, iconSize = iconSize)
        }
    }
}

@Composable
private fun IconComponent(
    @DrawableRes iconRes: Int?,
    iconSize: DpSize
) {
    iconRes?.let {
        Icon(
            painter = painterResource(it),
            contentDescription = null,
            tint = GlanciColors.onSurface,
            modifier = Modifier.size(iconSize)
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun SmallTertiaryButtonPreview() {
    PreviewContainer {
        SmallTertiaryButton(
            text = "View all",
            iconRes = R.drawable.short_arrow_right_icon,
            iconSize = DpSize(Dp.Unspecified, 16.dp),
            iconOrientation = IconOrientation.Right
        ) {}
    }
}
package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.model.IconOrientation
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun TertiaryButton(
    text: String,
    fontSize: TextUnit = 19.sp,
    @DrawableRes iconRes: Int? = null,
    iconSize: DpSize = DpSize(20.dp, 20.dp),
    iconOrientation: IconOrientation = IconOrientation.Left,
    enabled: Boolean = true,
    gradientColor: Pair<Color, Color> = GlanciColors.glassGradientPair,
    onClick: () -> Unit
) {
    val cornerSize = 21.dp

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClickEffect(shrinkScale = .98f, enabled = enabled, onClick = onClick)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientColor.second, gradientColor.first),
                    start = Offset(75f, 200f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassBorderGradient,
                    start = Offset(18f, 0f),
                    end = Offset(0f, 100f)
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(1.dp)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        if (iconOrientation == IconOrientation.Left) {
            IconComponent(iconRes = iconRes, iconSize = iconSize)
        }
        Text(
            text = text,
            color = GlanciColors.onSurface,
            fontSize = fontSize,
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
private fun TertiaryButtonPreview() {
    PreviewContainer {
        TertiaryButton(
            text = "Delete",
            iconRes = R.drawable.trash_icon,
            iconOrientation = IconOrientation.Left
        ) {}
    }
}
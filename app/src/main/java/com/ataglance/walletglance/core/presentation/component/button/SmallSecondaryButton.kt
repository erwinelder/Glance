package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun SmallSecondaryButton(
    text: String,
    @DrawableRes iconRes: Int? = null,
    enabled: Boolean = true,
    gradientColor: Pair<Color, Color> = GlanciColors.glassGradientPair,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    val cornerSize = 20.dp
    val borderGradient = if (enabled) GlanciColors.primarySemiTransparentGlassBorderGradient else
        listOf(GlanciColors.outline, GlanciColors.outline)

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GlanciColors.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GlanciColors.outline
        ),
        shape = RoundedCornerShape(cornerSize),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier
            .bounceClickEffect(.98f, enabled = enabled)
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
                    colors = borderGradient,
                    start = Offset(18f, 0f),
                    end = Offset(0f, 100f)
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(1.dp)
    ) {
        iconRes?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = "$text button icon",
                tint = GlanciColors.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = Manrope
        )
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewPrimarySmallButton() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallSecondaryButton(
            text = "ApplyApplyApply",
            iconRes = R.drawable.close_icon,
            onClick = {}
        )
    }
}
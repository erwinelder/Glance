package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun NavigationTextArrowButton(
    text: String,
    showLeftArrow: Boolean = false,
    fontSize: TextUnit = 18.sp,
    iconSize: Dp = 16.dp,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.bounceClickEffect()
    ) {
        if (showLeftArrow) {
            Icon(
                painter = painterResource(R.drawable.short_arrow_left_icon),
                contentDescription = "short left arrow",
                tint = GlanceColors.primary,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = text,
            color = GlanceColors.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope
        )
        if (!showLeftArrow) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "short right arrow",
                tint = GlanceColors.primary,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
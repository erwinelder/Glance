package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun NavigationTextArrowButton(
    text: String,
    fontSize: TextUnit = 20.sp,
    iconSize: Dp = 18.dp,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.bounceClickEffect()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = GlanceTheme.primary,
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                fontFamily = Manrope
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "left arrow",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
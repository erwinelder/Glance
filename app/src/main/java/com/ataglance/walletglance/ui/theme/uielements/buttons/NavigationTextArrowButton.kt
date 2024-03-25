package com.ataglance.walletglance.ui.theme.uielements.buttons

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.Manrope
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun NavigationTextArrowButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .bounceClickEffect(.97f)
    ) {
        Text(
            text = text,
            color = GlanceTheme.primary,
            fontSize = 19.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.short_arrow_right_icon),
            contentDescription = "left arrow",
            modifier = Modifier
                .size(12.dp, 18.dp)
        )
    }
}
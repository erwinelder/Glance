package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun BackButton(
    onClick: () -> Unit,
    text: String = stringResource(R.string.back)
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .bounceClickEffect(.97f)
    ) {
        Icon(
            painter = painterResource(R.drawable.short_arrow_left_icon),
            contentDescription = "left arrow",
            modifier = Modifier
                .size(12.dp, 18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = GlanceTheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope
        )
    }
}
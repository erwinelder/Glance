package com.ataglance.walletglance.core.presentation.component.button

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
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun CloseButton(
    onClick: () -> Unit,
    text: String = stringResource(R.string.close)
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .bounceClickEffect(.97f)
    ) {
        Icon(
            painter = painterResource(R.drawable.close_icon),
            contentDescription = "close icon",
            tint = GlanciColors.outline,
            modifier = Modifier
                .size(22.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = GlanciColors.outline,
            fontSize = 22.sp,
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal
        )
    }
}
package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun LoadingStateComponent(
    message: String,
    onCancel: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = message,
            color = GlanciColors.outline,
            fontSize = 18.sp,
            fontWeight = FontWeight.W400,
            fontFamily = Manrope
        )
        onCancel?.let {
            SmallSecondaryButton(
                text = stringResource(R.string.cancel),
                iconRes = R.drawable.close_icon,
                onClick = onCancel
            )
        }
    }
}
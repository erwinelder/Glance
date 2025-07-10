package com.ataglance.walletglance.core.presentation.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.NotoSans

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = GlanciColors.onSurface,
        style = GlanciTypography.titleLarge,
        fontFamily = NotoSans,
        modifier = modifier
    )
}
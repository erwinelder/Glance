package com.ataglance.walletglance.core.presentation.component.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.button.SmallTertiaryButton

@Composable
fun WidgetAdjustButton(
    onClick: () -> Unit
) {
    SmallTertiaryButton(
        text = stringResource(R.string.adjust),
        iconRes = R.drawable.settings_icon,
        onClick = onClick
    )
}
package com.ataglance.walletglance.core.presentation.component.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.button.SmallTertiaryButton
import com.ataglance.walletglance.core.presentation.model.IconOrientation

@Composable
fun WidgetViewAllButton(
    onClick: () -> Unit
) {
    SmallTertiaryButton(
        text = stringResource(R.string.view_all),
        iconRes = R.drawable.short_arrow_right_icon,
        iconSize = DpSize(Dp.Unspecified, 16.dp),
        iconOrientation = IconOrientation.Right,
        onClick = onClick
    )
}
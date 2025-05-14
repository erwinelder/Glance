package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R

@Composable
fun BackButton(
    onClick: () -> Unit,
    text: String = stringResource(R.string.back)
) {
    NavigationTextArrowButton(
        text = text,
        showLeftArrow = true,
        onClick = onClick
    )
}
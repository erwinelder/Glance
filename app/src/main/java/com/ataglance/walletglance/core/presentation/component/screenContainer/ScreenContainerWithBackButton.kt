package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.container.BackButtonBlock
import com.ataglance.walletglance.core.presentation.utils.plus

@Composable
fun ScreenContainerWithBackButton(
    screenPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    onBackButtonClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
        modifier = Modifier
            .padding(
                screenPadding + PaddingValues(
                    top = if (onBackButtonClick == null) 0.dp else 0.dp, bottom = 24.dp
                )
            )
            .fillMaxSize()
    ) {
        onBackButtonClick?.let { BackButtonBlock(it) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            content()
        }
    }
}
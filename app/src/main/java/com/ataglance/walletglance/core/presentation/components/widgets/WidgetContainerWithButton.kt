package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.components.buttons.NavigationTextArrowButton

@Composable
fun WidgetContainerWithButton(
    bottomNavigationText: String = stringResource(R.string.view_all),
    onBottomNavigationButtonClick: () -> Unit,
    widgetComponent: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        widgetComponent()
        NavigationTextArrowButton(
            text = bottomNavigationText,
            fontSize = 20.sp,
            onClick = onBottomNavigationButtonClick
        )
    }
}
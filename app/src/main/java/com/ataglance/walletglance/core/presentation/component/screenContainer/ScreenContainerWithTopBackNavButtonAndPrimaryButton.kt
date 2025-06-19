package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton

@Composable
fun ScreenContainerWithTopBackNavButtonAndPrimaryButton(
    screenPadding: PaddingValues = PaddingValues(),
    topBackNavButtonText: String,
    topBackNavButtonImageRes: Int? = null,
    onTopBackNavButtonClick: () -> Unit,
    topBackNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    primaryButtonText: String,
    primaryButtonEnabled: Boolean = true,
    onPrimaryButtonClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        topBackNavButtonText = topBackNavButtonText,
        topBackNavButtonImageRes = topBackNavButtonImageRes,
        onTopBackNavButtonClick = onTopBackNavButtonClick,
        topBackNavButtonCompanionComponent = topBackNavButtonCompanionComponent
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }

        PrimaryButton(
            text = primaryButtonText,
            enabled = primaryButtonEnabled,
            onClick = onPrimaryButtonClick
        )

    }
}

@Composable
fun ScreenContainerWithTopBackNavButtonAndPrimaryButton(
    screenPadding: PaddingValues = PaddingValues(),
    topBackNavButtonText: String,
    topBackNavButtonIconComponent: @Composable (() -> Unit)? = null,
    onTopBackNavButtonClick: () -> Unit,
    topBackNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    primaryButtonText: String,
    primaryButtonEnabled: Boolean = true,
    onPrimaryButtonClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        topBackNavButtonText = topBackNavButtonText,
        topBackNavButtonIconComponent = topBackNavButtonIconComponent,
        onTopBackNavButtonClick = onTopBackNavButtonClick,
        topBackNavButtonCompanionComponent = topBackNavButtonCompanionComponent
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }

        PrimaryButton(
            text = primaryButtonText,
            enabled = primaryButtonEnabled,
            onClick = onPrimaryButtonClick
        )

    }
}
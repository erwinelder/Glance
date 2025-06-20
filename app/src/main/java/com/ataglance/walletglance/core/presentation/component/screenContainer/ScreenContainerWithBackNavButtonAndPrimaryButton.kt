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
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilitySpacer

@Composable
fun ScreenContainerWithTopBackNavButtonAndPrimaryButton(
    screenPadding: PaddingValues = PaddingValues(),
    backNavButtonText: String,
    backNavButtonImageRes: Int? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    primaryButtonText: String,
    primaryButtonEnabled: Boolean = true,
    onPrimaryButtonClick: () -> Unit,
    content: @Composable ColumnScope.(keyboardInFocus: Boolean) -> Unit
) {
    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        backNavButtonText = backNavButtonText,
        backNavButtonImageRes = backNavButtonImageRes,
        onBackNavButtonClick = onBackNavButtonClick,
        backNavButtonCompanionComponent = backNavButtonCompanionComponent
    ) { keyboardInFocus ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content(keyboardInFocus)
            }
        }

        KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 24.dp)

        KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
            PrimaryButton(
                text = primaryButtonText,
                enabled = primaryButtonEnabled,
                onClick = onPrimaryButtonClick
            )
        }

    }
}

@Composable
fun ScreenContainerWithTopBackNavButtonAndPrimaryButton(
    screenPadding: PaddingValues = PaddingValues(),
    backNavButtonText: String,
    backNavButtonIconComponent: @Composable (() -> Unit)? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    primaryButtonText: String,
    primaryButtonEnabled: Boolean = true,
    onPrimaryButtonClick: () -> Unit,
    content: @Composable ColumnScope.(keyboardInFocus: Boolean) -> Unit
) {
    ScreenContainerWithTopBackNavButton(
        screenPadding = screenPadding,
        backNavButtonText = backNavButtonText,
        backNavButtonIconComponent = backNavButtonIconComponent,
        onBackNavButtonClick = onBackNavButtonClick,
        backNavButtonCompanionComponent = backNavButtonCompanionComponent
    ) { keyboardInFocus ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content(keyboardInFocus)
            }
        }

        KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 24.dp)

        KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
            PrimaryButton(
                text = primaryButtonText,
                enabled = primaryButtonEnabled,
                onClick = onPrimaryButtonClick
            )
        }

    }
}
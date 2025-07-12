package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilitySpacer
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.utils.copy
import com.ataglance.walletglance.core.presentation.utils.getKeyboardBottomPaddingAnimated
import com.ataglance.walletglance.core.presentation.utils.isKeyboardVisible
import com.ataglance.walletglance.core.presentation.utils.plus

@Composable
fun ScreenContainerWithTopBackNavButton(
    screenPadding: PaddingValues = PaddingValues(),
    bottomPadding: Dp = 16.dp,
    backNavButtonText: String,
    backNavButtonImageRes: Int? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    gap: Dp = 24.dp,
    content: @Composable ColumnScope.(keyboardInFocus: Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by isKeyboardVisible()
    val bottomPadding by getKeyboardBottomPaddingAnimated(minPadding = bottomPadding)

    val screenBottomPadding by animateDpAsState(
        targetValue = if (isKeyboardVisible) 0.dp else screenPadding.bottom
    )
    val screenPadding = screenPadding.copy(bottom = 0.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(
                paddingValues = screenPadding + PaddingValues(
                    top = 8.dp,
                    bottom = bottomPadding + screenBottomPadding
                )
            )
    ) {

        KeyboardTypingAnimatedVisibilityContainer(isVisible = !isKeyboardVisible) {
            GlassSurfaceTopNavButtonBlock(
                text = backNavButtonText,
                imageRes = backNavButtonImageRes,
                onClick = onBackNavButtonClick,
                companionComponent = backNavButtonCompanionComponent
            )
        }

        KeyboardTypingAnimatedVisibilitySpacer(isVisible = !isKeyboardVisible, height = gap)

        content(isKeyboardVisible)

    }
}

@Composable
fun ScreenContainerWithTopBackNavButton(
    screenPadding: PaddingValues = PaddingValues(),
    bottomPadding: Dp = 16.dp,
    backNavButtonText: String,
    backNavButtonIconComponent: @Composable (() -> Unit)? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    gap: Dp = 24.dp,
    content: @Composable ColumnScope.(keyboardInFocus: Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by isKeyboardVisible()
    val bottomPadding by getKeyboardBottomPaddingAnimated(minPadding = bottomPadding)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { focusManager.clearFocus() }
            .fillMaxSize()
            .padding(
                paddingValues = screenPadding + PaddingValues(top = 8.dp, bottom = bottomPadding)
            )
    ) {

        KeyboardTypingAnimatedVisibilityContainer(isVisible = !isKeyboardVisible) {
            GlassSurfaceTopNavButtonBlock(
                text = backNavButtonText,
                iconComponent = backNavButtonIconComponent,
                onClick = onBackNavButtonClick,
                companionComponent = backNavButtonCompanionComponent
            )
        }

        KeyboardTypingAnimatedVisibilitySpacer(isVisible = !isKeyboardVisible, height = gap)

        content(isKeyboardVisible)

    }
}
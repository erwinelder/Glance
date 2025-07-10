package com.ataglance.walletglance.request.presentation.component.screenContainer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.LoadingStateComponent
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.icon.AnimatedIconWithTitle
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainer
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.utils.getKeyboardBottomPaddingAnimated
import com.ataglance.walletglance.core.presentation.utils.isKeyboardVisible
import com.ataglance.walletglance.request.presentation.component.container.ResultStateButtonComponent
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState

@Composable
fun AnimatedRequestScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    iconPathsRes: IconPathsRes,
    title: String? = null,
    requestStateButton: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: (() -> Unit)? = null,
    onSuccessButton: (() -> Unit)? = null,
    onErrorButton: () -> Unit,
    screenTopContent: @Composable (() -> Unit)? = null,
    screenCenterContent: @Composable ((isKeyboardVisible: Boolean) -> Unit)? = null,
    screenBottomContent: @Composable (() -> Unit)? = null
) {
    val iconGradientColor = when (requestStateButton) {
        null, is RequestState.Loading, is RequestState.Success ->
            GlanciColors.iconPrimaryGlassGradientPair
        is RequestState.Error -> GlanciColors.iconErrorGlassGradientPair
    }
    val iconSize = when (requestStateButton) {
        null -> 48.dp
        is RequestState.Loading -> 80.dp
        is RequestState.Success, is RequestState.Error -> 104.dp
    }

    val weight by animateFloatAsState(
        targetValue = when {
            requestStateButton != null -> 0.01f
            else -> if (screenBottomContent != null) 1f else 0.01f
        }
    )

    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by isKeyboardVisible()
    val bottomPadding by getKeyboardBottomPaddingAnimated(minPadding = 24.dp)


    SetBackHandler(enabled = requestStateButton != null) {}

    ScreenContainer(
        screenPadding = screenPadding,
        padding = PaddingValues(top = 8.dp, bottom = bottomPadding),
        modifier = Modifier.clickable { focusManager.clearFocus() }
    ) {

        AnimatedContent(
            targetState = requestStateButton,
            contentAlignment = Alignment.Center
        ) { requestState ->
            if (requestState == null && screenTopContent != null) {
                KeyboardTypingAnimatedVisibilityContainer(isVisible = !isKeyboardVisible) {
                    screenTopContent()
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {

            Spacer(modifier = Modifier.weight(1f))

            if (title != null) {

                KeyboardTypingAnimatedVisibilityContainer(
                    isVisible = !isKeyboardVisible,
                    modifier = Modifier
                        .fillMaxWidth(FilledWidthByScreenType().get(CurrWindowType))
                        .padding(bottom = 8.dp)
                ) {
                    AnimatedIconWithTitle(
                        iconPathsRes = iconPathsRes,
                        title = title,
                        animate = requestStateButton is RequestState.Loading,
                        isTitleVisible = requestStateButton == null,
                        iconGradientColor = iconGradientColor,
                        iconSize = iconSize
                    )
                }

                Spacer(modifier = Modifier.weight(weight))

            }

            AnimatedContent(
                targetState = requestStateButton,
                contentAlignment = Alignment.Center
            ) { requestState ->
                when (requestState) {
                    null -> {
                        screenCenterContent?.invoke(isKeyboardVisible)
                    }
                    is RequestState.Loading -> {
                        LoadingStateComponent(
                            message = stringResource(requestState.messageRes),
                            onCancel = onCancelRequest,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    is RequestState.Success -> {
                        if (onSuccessButton != null) {
                            ResultStateButtonComponent(
                                state = requestState.state,
                                usePrimaryButtonInstead = true,
                                onButtonClick = onSuccessButton
                            )
                        }
                    }
                    is RequestState.Error -> {
                        ResultStateButtonComponent(
                            state = requestState.state,
                            usePrimaryButtonInstead = false,
                            onButtonClick = onErrorButton
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

        }

        AnimatedContent(
            targetState = requestStateButton,
            contentAlignment = Alignment.Center
        ) { requestState ->
            if (requestState == null && screenBottomContent != null) {
                KeyboardTypingAnimatedVisibilityContainer(isVisible = !isKeyboardVisible) {
                    screenBottomContent()
                }
            }
        }

    }
}
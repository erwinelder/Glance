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
import com.ataglance.walletglance.core.presentation.model.RotatingGradientAnimState
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.utils.getKeyboardBottomPaddingAnimated
import com.ataglance.walletglance.core.presentation.utils.isKeyboardVisible
import com.ataglance.walletglance.request.presentation.component.container.ResultStateButtonComponent
import com.ataglance.walletglance.request.presentation.model.RequestErrorState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState

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
    val animState = when (requestStateButton) {
        null -> RotatingGradientAnimState.Idle
        is RequestState.Loading -> RotatingGradientAnimState.Active
        is RequestState.Success, is RequestState.Error -> RotatingGradientAnimState.Calm
    }
    val iconGradientColor = when (requestStateButton) {
        null, is RequestState.Loading, is RequestState.Success ->
            GlanciColors.iconPrimaryGlassGradientPair
        is RequestState.Error -> GlanciColors.iconErrorGlassGradientPair
    }

    val weight by animateFloatAsState(
        targetValue = when {
            requestStateButton != null -> 0.01f
            else -> 1f
        }
    )

    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by isKeyboardVisible()
    val bottomPadding by getKeyboardBottomPaddingAnimated(minPadding = 24.dp)


    SetBackHandler(enabled = requestStateButton != null) {}

    ScreenContainer(
        screenPadding = screenPadding,
        padding = PaddingValues(bottom = bottomPadding),
        modifier = Modifier.clickable { focusManager.clearFocus() }
    ) {

        AnimatedContent(
            targetState = requestStateButton,
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 8.dp)
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
                        .fillMaxWidth(FilledWidthByScreenType(.84f).get(CurrWindowType))
                        .padding(vertical = 16.dp)
                ) {
                    AnimatedIconWithTitle(
                        iconPathsRes = iconPathsRes,
                        title = title,
                        animState = animState,
                        isTitleVisible = requestStateButton == null,
                        iconGradientColor = iconGradientColor
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
                            onCancel = onCancelRequest
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
                KeyboardTypingAnimatedVisibilityContainer(
                    isVisible = !isKeyboardVisible,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    screenBottomContent()
                }
            }
        }

    }
}

@Composable
fun AnimatedRequestScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    iconPathsRes: IconPathsRes,
    title: String? = null,
    requestErrorStateButton: RequestErrorState<ButtonState>?,
    onCancelRequest: (() -> Unit)? = null,
    onErrorButton: () -> Unit,
    screenTopContent: @Composable (() -> Unit)? = null,
    screenCenterContent: @Composable ((isKeyboardVisible: Boolean) -> Unit)? = null,
    screenBottomContent: @Composable (() -> Unit)? = null
) {
    val animState = when (requestErrorStateButton) {
        null -> RotatingGradientAnimState.Idle
        is RequestErrorState.Loading -> RotatingGradientAnimState.Active
        is RequestErrorState.Error -> RotatingGradientAnimState.Calm
    }
    val iconGradientColor = when (requestErrorStateButton) {
        null, is RequestErrorState.Loading ->
            GlanciColors.iconPrimaryGlassGradientPair
        is RequestErrorState.Error -> GlanciColors.iconErrorGlassGradientPair
    }

    val weight by animateFloatAsState(
        targetValue = when {
            requestErrorStateButton != null -> 0.01f
            else -> 1f
        }
    )

    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by isKeyboardVisible()
    val bottomPadding by getKeyboardBottomPaddingAnimated(minPadding = 24.dp)


    SetBackHandler(enabled = requestErrorStateButton != null) {}

    ScreenContainer(
        screenPadding = screenPadding,
        padding = PaddingValues(bottom = bottomPadding),
        modifier = Modifier.clickable { focusManager.clearFocus() }
    ) {

        AnimatedContent(
            targetState = requestErrorStateButton,
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 8.dp)
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
                        .fillMaxWidth(FilledWidthByScreenType(.84f).get(CurrWindowType))
                        .padding(vertical = 16.dp)
                ) {
                    AnimatedIconWithTitle(
                        iconPathsRes = iconPathsRes,
                        title = title,
                        animState = animState,
                        isTitleVisible = requestErrorStateButton == null,
                        iconGradientColor = iconGradientColor
                    )
                }

                Spacer(modifier = Modifier.weight(weight))

            }

            AnimatedContent(
                targetState = requestErrorStateButton,
                contentAlignment = Alignment.Center
            ) { requestState ->
                when (requestState) {
                    null -> {
                        screenCenterContent?.invoke(isKeyboardVisible)
                    }
                    is RequestErrorState.Loading -> {
                        LoadingStateComponent(
                            message = stringResource(requestState.messageRes),
                            onCancel = onCancelRequest
                        )
                    }
                    is RequestErrorState.Error -> {
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
            targetState = requestErrorStateButton,
            contentAlignment = Alignment.Center
        ) { requestState ->
            if (requestState == null && screenBottomContent != null) {
                KeyboardTypingAnimatedVisibilityContainer(
                    isVisible = !isKeyboardVisible,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    screenBottomContent()
                }
            }
        }

    }
}
package com.ataglance.walletglance.core.presentation.component.screenContainer.state

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState

@Composable
fun AnimatedScreenWithRequestState(
    screenPadding: PaddingValues = PaddingValues(),
    requestState: RequestState<ButtonState>?,
    onCancelRequest: (() -> Unit)? = null,
    onSuccessClose: () -> Unit = {},
    onErrorClose: () -> Unit,
    screen: @Composable () -> Unit
) {
    SetBackHandler(enabled = requestState != null) {}

    AnimatedContent(
        targetState = requestState,
        modifier = Modifier
            .fillMaxSize()
            .padding(screenPadding)
    ) { state ->
        if (state == null) {
            screen()
        } else {
            RequestStateScreen(
                state = state,
                onCancelRequest = onCancelRequest,
                onSuccessClose = onSuccessClose,
                onErrorClose = onErrorClose
            )
        }
    }
}
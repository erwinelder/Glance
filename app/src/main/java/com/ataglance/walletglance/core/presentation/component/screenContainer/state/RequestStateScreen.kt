package com.ataglance.walletglance.core.presentation.component.screenContainer.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.core.presentation.component.container.LoadingStateComponent
import com.ataglance.walletglance.errorHandling.presentation.component.container.ResultStateComponentWithButton
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState

@Composable
fun RequestStateScreen(
    state: RequestState<ButtonState>,
    onCancelRequest: (() -> Unit)? = null,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is RequestState.Loading -> {
                LoadingStateComponent(
                    message = stringResource(state.messageRes),
                    onCancel = onCancelRequest
                )
            }
            is RequestState.Result -> {
                ResultStateComponentWithButton(
                    resultState = state.resultState,
                    onSuccessClose = onSuccessClose,
                    onErrorClose = onErrorClose
                )
            }
        }
    }
}
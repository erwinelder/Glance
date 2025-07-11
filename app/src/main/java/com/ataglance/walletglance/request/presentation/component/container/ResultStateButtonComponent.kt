package com.ataglance.walletglance.request.presentation.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.text.TitleWithMessageComponent
import com.ataglance.walletglance.request.presentation.model.ResultState

@Composable
fun ResultStateButtonComponent(
    state: ResultState.ButtonState,
    usePrimaryButtonInstead: Boolean = true,
    onButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        TitleWithMessageComponent(
            title = stringResource(state.titleRes),
            message = state.messageRes?.let { stringResource(it) }
        )

        if (usePrimaryButtonInstead) {
            SmallPrimaryButton(
                text = stringResource(state.buttonTextRes),
                iconRes = state.buttonIconRes,
                onClick = onButtonClick
            )
        } else {
            SmallSecondaryButton(
                text = stringResource(state.buttonTextRes),
                iconRes = state.buttonIconRes,
                onClick = onButtonClick
            )
        }

    }
}
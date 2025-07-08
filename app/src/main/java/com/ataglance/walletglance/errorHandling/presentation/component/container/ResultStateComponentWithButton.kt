package com.ataglance.walletglance.errorHandling.presentation.component.container

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState

@Composable
fun ResultStateComponentWithButton(
    resultState: ButtonState,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    val iconRes = if (resultState.isSuccessful)
        R.drawable.success_large_icon else R.drawable.error_large_icon
    val iconDescription = if (resultState.isSuccessful) "Success icon" else "Error icon"
    val iconGradient = if (resultState.isSuccessful)
        GlanciColors.primaryGlassGradient else GlanciColors.errorGradient

    ResultComponentWithButton(
        iconRes = iconRes,
        iconDescription = iconDescription,
        iconGradient = iconGradient,
        title = stringResource(resultState.titleRes),
        message = resultState.messageRes?.let { stringResource(it) },
        buttonText = stringResource(resultState.buttonTextRes),
        buttonIconRes = resultState.buttonIconRes,
        usePrimaryButtonInstead = resultState.isSuccessful,
        onButtonClick = {
            if (resultState.isSuccessful) onSuccessClose() else onErrorClose()
        }
    )
}
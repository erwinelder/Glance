package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.ScreenContainer
import com.ataglance.walletglance.errorHandling.presentation.components.containers.SuccessMessageWithIcon

@Composable
fun ResultMessageScreenContainer(
    message: String,
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ScreenContainer {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SuccessMessageWithIcon(message = message)
        }
        PrimaryButton(
            text = buttonText,
            onClick = onContinueButtonClick
        )
    }
}
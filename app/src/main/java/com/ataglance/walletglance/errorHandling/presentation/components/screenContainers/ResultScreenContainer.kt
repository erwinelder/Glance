package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.ScreenContainer
import com.ataglance.walletglance.errorHandling.presentation.components.containers.SuccessMessageWithIcon

@Composable
fun ResultScreenContainer(
    message: String,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconBackgroundGradient: List<Color> = GlanceColors.primaryGradient,
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
            SuccessMessageWithIcon(
                message = message,
                iconRes = iconRes,
                iconDescription = iconDescription,
                iconBackgroundGradient = iconBackgroundGradient
            )
        }
        PrimaryButton(
            text = buttonText,
            onClick = onContinueButtonClick
        )
    }
}
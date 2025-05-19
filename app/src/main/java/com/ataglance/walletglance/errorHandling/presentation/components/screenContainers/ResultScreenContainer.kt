package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.LargePrimaryIconWithMessage
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithBackButton
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun ResultScreenContainer(
    title: String,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconBackgroundGradient: List<Color> = GlanceColors.primaryGradient,
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ScreenContainerWithBackButton {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LargePrimaryIconWithMessage(
                title = title,
                iconRes = iconRes,
                iconDescription = iconDescription,
                gradientColor = iconBackgroundGradient,
            )
        }
        PrimaryButton(
            text = buttonText,
            onClick = onContinueButtonClick
        )
    }
}
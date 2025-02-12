package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun ResultErrorScreenContainer(
    message: String,
    @DrawableRes iconRes: Int = R.drawable.error_large_icon,
    iconDescription: String = "Error",
    iconBackgroundGradient: List<Color> = GlanceColors.errorGradient,
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ResultScreenContainer(
        message = message,
        iconRes = iconRes,
        iconDescription = iconDescription,
        iconBackgroundGradient = iconBackgroundGradient,
        buttonText = buttonText,
        onContinueButtonClick = onContinueButtonClick
    )
}
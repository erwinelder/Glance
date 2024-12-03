package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun ErrorResultScreenContainer(
    message: String,
    @DrawableRes iconRes: Int = R.drawable.error_icon,
    iconDescription: String = "Error",
    iconBackgroundGradient: List<Color> = GlanceTheme.errorGradient.toList(),
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ResultMessageScreenContainer(
        message = message,
        iconRes = iconRes,
        iconDescription = iconDescription,
        iconBackgroundGradient = iconBackgroundGradient,
        buttonText = buttonText,
        onContinueButtonClick = onContinueButtonClick
    )
}
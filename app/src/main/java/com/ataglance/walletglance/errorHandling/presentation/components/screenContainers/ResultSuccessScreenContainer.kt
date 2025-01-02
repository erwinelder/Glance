package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun ResultSuccessScreenContainer(
    message: String,
    @DrawableRes iconRes: Int = R.drawable.success_large_icon,
    iconDescription: String = "Success",
    iconBackgroundGradient: List<Color> = GlanceTheme.primaryGradientLightToDark.toList(),
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
package com.ataglance.walletglance.errorHandling.presentation.components.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun ResultSuccessScreenContainer(
    title: String,
    @DrawableRes iconRes: Int = R.drawable.success_large_icon,
    iconDescription: String = "Success",
    iconBackgroundGradient: List<Color> = GlanceColors.primaryGradient,
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ResultScreenContainer(
        title = title,
        iconRes = iconRes,
        iconDescription = iconDescription,
        iconBackgroundGradient = iconBackgroundGradient,
        buttonText = buttonText,
        onContinueButtonClick = onContinueButtonClick
    )
}
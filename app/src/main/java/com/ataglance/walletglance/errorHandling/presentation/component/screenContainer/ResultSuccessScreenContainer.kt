package com.ataglance.walletglance.errorHandling.presentation.component.screenContainer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun ResultSuccessScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    title: String,
    @DrawableRes iconRes: Int = R.drawable.success_large_icon,
    iconDescription: String = "Success",
    iconBackgroundGradient: List<Color> = GlanciColors.primaryGlassGradient,
    buttonText: String,
    onContinueButtonClick: () -> Unit
) {
    ResultScreenContainer(
        screenPadding = screenPadding,
        title = title,
        iconRes = iconRes,
        iconDescription = iconDescription,
        iconBackgroundGradient = iconBackgroundGradient,
        buttonText = buttonText,
        onContinueButtonClick = onContinueButtonClick
    )
}
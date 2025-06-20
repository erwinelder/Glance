package com.ataglance.walletglance.errorHandling.presentation.component.container

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.icon.LargePrimaryIconWithMessage

@Composable
fun ResultComponentWithButton(
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconGradient: List<Color>,
    title: String,
    message: String? = null,
    buttonText: String = stringResource(R.string.close),
    @DrawableRes buttonIconRes: Int? = R.drawable.close_icon,
    usePrimaryButtonInstead: Boolean = false,
    onButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LargePrimaryIconWithMessage(
            title = title,
            message = message,
            iconRes = iconRes,
            iconDescription = iconDescription,
            gradientColor = iconGradient
        )
        if (usePrimaryButtonInstead) {
            SmallPrimaryButton(
                text = buttonText,
                iconRes = buttonIconRes,
                onClick = onButtonClick
            )
        } else {
            SmallSecondaryButton(
                text = buttonText,
                iconRes = buttonIconRes,
                onClick = onButtonClick
            )
        }
    }
}
package com.ataglance.walletglance.request.presentation.component.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.icon.AnimatedIconWithTitle
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainer
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.model.RotatingGradientAnimState

@Composable
fun ResultScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    iconPathsRes: IconPathsRes,
    title: String,
    iconGradientColor: Pair<Color, Color>,
    buttonText: String,
    onPrimaryButtonClick: () -> Unit
) {
    ScreenContainer(screenPadding = screenPadding) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AnimatedIconWithTitle(
                iconPathsRes = iconPathsRes,
                title = title,
                animState = RotatingGradientAnimState.Calm,
                iconGradientColor = iconGradientColor,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
        }
        PrimaryButton(
            text = buttonText,
            onClick = onPrimaryButtonClick
        )
    }
}
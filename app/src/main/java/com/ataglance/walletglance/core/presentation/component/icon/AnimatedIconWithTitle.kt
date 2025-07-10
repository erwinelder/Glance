package com.ataglance.walletglance.core.presentation.component.icon

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.text.Title
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun AnimatedIconWithTitle(
    iconPathsRes: IconPathsRes,
    title: String,
    animate: Boolean = false,
    isTitleVisible: Boolean = true,
    iconGradientColor: Pair<Color, Color> = GlanciColors.iconPrimaryGlassGradientPair,
    iconSize: Dp = 48.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RotatingGradientIcon(
            iconPathsRes = iconPathsRes,
            animate = animate,
            iconGradientColor = iconGradientColor,
            iconSize = iconSize
        )
        AnimatedContent(
            targetState = isTitleVisible,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(220, delayMillis = 90)
                        ))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            },
            contentAlignment = Alignment.Center
        ) { isIdle ->
            if (isIdle) {
                Title(text = title)
            }
        }
    }
}
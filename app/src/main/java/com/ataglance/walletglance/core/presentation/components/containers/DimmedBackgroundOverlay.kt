package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.domain.app.AppTheme

@Composable
fun DimmedBackgroundOverlay(visible: Boolean, appTheme: AppTheme?) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(
                        when (appTheme) {
                            AppTheme.LightDefault -> .2f
                            AppTheme.DarkDefault -> .4f
                            else -> .0f
                        }
                    )
                )
        )
    }
}
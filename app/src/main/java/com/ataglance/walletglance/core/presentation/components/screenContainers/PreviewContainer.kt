package com.ataglance.walletglance.core.presentation.components.screenContainers

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.WalletGlanceTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewContainer(
    appTheme: AppTheme = AppTheme.LightDefault,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints {
        SharedTransitionLayout {
            WalletGlanceTheme(
                useDeviceTheme = false,
                lastChosenTheme = appTheme.name,
                boxWithConstraintsScope = this@BoxWithConstraints,
                sharedTransitionScope = this@SharedTransitionLayout
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(
                            when (appTheme) {
                                AppTheme.LightDefault -> R.drawable.main_background_light
                                AppTheme.DarkDefault -> R.drawable.main_background_dark
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    content()
                }
            }
        }
    }
}
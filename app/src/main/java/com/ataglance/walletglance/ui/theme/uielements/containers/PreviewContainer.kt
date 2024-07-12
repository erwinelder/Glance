package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme

@Composable
fun PreviewContainer(
    appTheme: AppTheme = AppTheme.LightDefault,
    content: @Composable () -> Unit
) {
    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = appTheme.name,
            boxWithConstraintsScope = this
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
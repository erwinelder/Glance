package com.ataglance.walletglance

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.modifiers.NoRippleTheme
import com.ataglance.walletglance.ui.theme.screens.AppScreen
import com.ataglance.walletglance.ui.theme.theme.AppTheme

class MainActivity : AppCompatActivity() {
    private lateinit var app: WalletGlanceApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as WalletGlanceApplication
        val appViewModel = app.appViewModel
        appViewModel.fetchDataOnStart()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.themeUiState.value == null ||
                        appViewModel.appUiSettings.value.appTheme == null
            }
        }

        setContent {
            val context = LocalContext.current as ComponentActivity
            val appUiSettings = appViewModel.appUiSettings.collectAsStateWithLifecycle().value
            val themeUiState = appViewModel.themeUiState.collectAsStateWithLifecycle().value

            BoxWithConstraints(modifier = Modifier.safeDrawingPadding()) {
                if (themeUiState != null) {
                    WalletGlanceTheme(
                        context = context,
                        useDeviceTheme = themeUiState.useDeviceTheme,
                        chosenLightTheme = themeUiState.chosenLightTheme,
                        chosenDarkTheme = themeUiState.chosenDarkTheme,
                        lastChosenTheme = themeUiState.lastChosenTheme,
                        setIsDarkTheme = appViewModel::updateAppThemeState,
                        boxWithConstraintsScope = this
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GlanceTheme.background)
                        ) {
                            AnimatedContent(
                                targetState = appUiSettings.appTheme,
                                label = "App background",
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                }
                            ) { targetAppTheme ->
                                when (targetAppTheme) {
                                    AppTheme.LightDefault -> {
                                        Image(
                                            painter = painterResource(R.drawable.main_background_light),
                                            contentDescription = "application background",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    AppTheme.DarkDefault -> {
                                        Image(
                                            painter = painterResource(R.drawable.main_background_dark),
                                            contentDescription = "application background",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    else -> {
                                        Box(modifier = Modifier.fillMaxSize())
                                    }
                                }
                            }
                            CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                                AppScreen(
                                    appViewModel = appViewModel,
                                    appUiSettings = appUiSettings,
                                    themeUiState = themeUiState
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(ContentValues.TAG, "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(ContentValues.TAG, "onResume called")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d(ContentValues.TAG, "onRestart called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(ContentValues.TAG, "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(ContentValues.TAG, "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(ContentValues.TAG, "onDestroy called")
    }
}
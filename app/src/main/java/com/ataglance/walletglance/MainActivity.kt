package com.ataglance.walletglance

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
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
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.modifiers.NoRippleTheme
import com.ataglance.walletglance.ui.theme.screens.AppScreen
import com.ataglance.walletglance.ui.theme.theme.AppTheme

class MainActivity : AppCompatActivity() {
    private lateinit var app: WalletGlanceApplication

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d(ContentValues.TAG, "onCreate called")

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
            val appUiState = appViewModel.appUiSettings.collectAsStateWithLifecycle().value
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
                        AnimatedVisibility(
                            visible = appUiState.appTheme != null,
                            enter = fadeIn(tween(1000))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // app background
                                Image(
                                    painter = painterResource(
                                        if (appUiState.appTheme == AppTheme.LightDefault) R.drawable.main_background_light
                                        else R.drawable.main_background_dark
                                    ),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                                CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                                    AppScreen(
                                        appViewModel = appViewModel,
                                        appUiSettings = appUiState,
                                        themeUiState = themeUiState
                                    )
                                }
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
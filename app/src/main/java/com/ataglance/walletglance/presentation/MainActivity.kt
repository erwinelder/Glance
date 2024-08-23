package com.ataglance.walletglance.presentation

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ataglance.walletglance.WalletGlanceApplication
import com.ataglance.walletglance.presentation.ui.WalletGlanceAppComponent
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeAppViewModel()

        setupSplashScreen()

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides this) {
                WalletGlanceAppComponent(appViewModel = appViewModel)
            }
        }
    }

    private fun initializeAppViewModel() {
        val app: WalletGlanceApplication = application as WalletGlanceApplication
        appViewModel = app.appViewModel
        appViewModel.fetchDataOnStart()
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.themeUiState.value == null ||
                        appViewModel.appUiSettings.value.appTheme == null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(ContentValues.TAG, "onStart called")
    }
    override fun onResume() {
        super.onResume()
        appViewModel.updateGreetingsWidgetTitle()
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
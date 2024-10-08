package com.ataglance.walletglance.core.presentation

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ataglance.walletglance.GlanceApplication
import com.ataglance.walletglance.core.presentation.components.WalletGlanceAppComponent
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var app: GlanceApplication
    private lateinit var appViewModel: AppViewModel
    private lateinit var navViewModel: NavigationViewModel
    private lateinit var personalizationViewModel: PersonalizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as GlanceApplication
        initializeAppViewModel()
        initializeNavViewModel()
        initializePersonalizationViewModel()

        setupSplashScreen()

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides this) {
                WalletGlanceAppComponent(
                    appViewModel = appViewModel,
                    navViewModel = navViewModel,
                    personalizationViewModel = personalizationViewModel
                )
            }
        }
    }

    private fun initializeAppViewModel() {
        appViewModel = app.appViewModel
        appViewModel.fetchDataOnStart()
    }

    private fun initializeNavViewModel() {
        navViewModel = app.navViewModel
        navViewModel.fetchBottomBarNavigationButtons()
    }

    private fun initializePersonalizationViewModel() {
        personalizationViewModel = app.personalizationViewModel
        personalizationViewModel.fetchDataOnStart()
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
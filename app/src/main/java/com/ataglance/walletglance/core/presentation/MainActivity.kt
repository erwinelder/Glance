package com.ataglance.walletglance.core.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.presentation.navigation.AuthScreens
import com.ataglance.walletglance.billing.domain.mapper.asAppSubscription
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.core.presentation.components.GlanceAppComponent
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by inject()
    private val firestore: FirebaseFirestore by inject()
    private val authController: AuthController by inject()
    private val appViewModel: AppViewModel by inject()
    private val navViewModel: NavigationViewModel by inject()
    private val personalizationViewModel: PersonalizationViewModel by inject()
    private lateinit var navController: NavHostController
    private val billingSubscriptionManager: BillingSubscriptionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val coroutineScope: CoroutineScope by inject()

        super.onCreate(savedInstanceState)
        handleDeepLink(intent)

        doInitialSetup()
        setupSplashScreen()
        initializeFirebaseDebugger()

        coroutineScope.launch {
            billingSubscriptionManager.newPurchase.collect { purchaseResult ->
                purchaseResult.getDataIfSuccess()
                    ?.asAppSubscription()
                    ?.let(authController::setUserSubscription)
            }
        }

        coroutineScope.launch {
            authController.userState.collect {
                getKoin().getScopeOrNull(scopeId = "userScope")?.close()
                getKoin().createScope(scopeId = "userScope", qualifier = named("UserScope"))
            }
        }

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides this) {
                navController = rememberNavController()

                GlanceAppComponent(
                    authController = authController,
                    billingSubscriptionManager = billingSubscriptionManager,
                    appViewModel = appViewModel,
                    navViewModel = navViewModel,
                    navController = navController,
                    personalizationViewModel = personalizationViewModel
                )
            }
        }
    }

    private fun doInitialSetup() {
        appViewModel.applyAppLanguage()
        appViewModel.updateSetupStageIfNeeded()
        appViewModel.fetchDataOnStart()
        navViewModel.fetchBottomBarNavigationButtons()
        personalizationViewModel.fetchDataOnStart()
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.themeUiState.value == null ||
                        appViewModel.appConfiguration.value.appTheme == null
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val uri = intent.data ?: return

        when (val mode = uri.getQueryParameter("mode")) {
            "resetPassword" -> {
                val obbCode = uri.getQueryParameter("obbCode").takeUnless { it.isNullOrEmpty() }
                if (obbCode != null) {
                    navViewModel.popBackStackAndNavigateToResetPasswordScreen(
                        navController = navController, obbCode = obbCode
                    )
                } else {
                    Log.e("Reset password link", "No oobCode found in the deep link")
                }
            }
            "verifyEmail" -> {
                val obbCode = uri.getQueryParameter("obbCode").takeUnless { it.isNullOrEmpty() }
                lifecycleScope.launch {
                    if (obbCode != null) {
                        processEmailVerification(obbCode)
                    } else {
                        Log.e("Email verification link", "No oobCode found in the deep link")
                    }
                }
            }
            else -> {
                Log.e("Deep link", "Unknown deep link mode or action: $mode")
            }
        }
    }

    private suspend fun processEmailVerification(obbCode: String) {
        val screen = when (authController.applyObbCode(obbCode)) {
            true -> AuthScreens.ResultSuccess(
                screenType = AuthResultSuccessScreenType.EmailVerification.name
            )
            false -> AuthScreens.EmailVerificationFailed
        }

        navViewModel.popBackStackAndNavigateToScreen(
            navController = navController,
            screen = screen
        )
    }


    private fun initializeFirebaseDebugger() {
        if (BuildConfig.DEBUG) {
            auth.useEmulator("10.0.2.2", 9099)
            firestore.useEmulator("10.0.2.2", 8080)
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
package com.ataglance.walletglance.core.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.navigation.AuthScreens
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.core.presentation.components.GlanceAppComponent
import com.ataglance.walletglance.core.utils.extractOobCode
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by inject()
    private val authController: AuthController by inject()
    private lateinit var navController: NavHostController
    private lateinit var billingSubscriptionManager: BillingSubscriptionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()

        billingSubscriptionManager = GlobalContext.get().get()

        super.onCreate(savedInstanceState)

//        val coroutineScope = CoroutineScope(Dispatchers.IO)

        /*coroutineScope.launch {
            billingSubscriptionManager.newPurchase.collect { purchaseResult ->
                purchaseResult.getDataIfSuccess()
                    ?.asAppSubscription()
                    ?.let(authController::setUserSubscription)
            }
        }*/

        initializeFirebaseDebugger()

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides this) {
                navController = rememberNavController()

                LaunchedEffect(true) {
                    navController.currentBackStackEntryFlow.first()
                    handleDeepLink(intent)
                }

                LaunchedEffect(true) {
                    authController.fetchUserDataAndUpdateUser()
                }

                GlanceAppComponent(navController = navController)
            }
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                false
            //                appViewModel.themeUiState.value == null ||
//                        appViewModel.appConfiguration.value.appTheme == null
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
            "verifyEmail" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()
                        ?.let { processEmailVerificationLink(it) }
                        ?: Log.e("Email verification link", "No oobCode found in the deep link")
                }
            }
            "verifyAndChangeEmail" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()
                        ?.let { processEmailVerificationAndChangeLink(it) }
                        ?: Log.e("Email verification link", "No oobCode found in the deep link")
                }
            }
            "resetPassword" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()
                        ?.let { processResetPasswordLink(it) }
                        ?: Log.e("Reset password link", "No oobCode found in the deep link")
                }
            }
            else -> Log.e("Deep link", "Unknown deep link mode or action: $mode")
        }
    }

    private suspend fun processEmailVerificationLink(obbCode: String) {
        val screen = when (authController.applyOobCode(obbCode)) {
            true -> AuthScreens.ResultSuccess(
                screenType = AuthResultSuccessScreenType.EmailVerification.name
            )
            false -> AuthScreens.EmailVerificationFailed
        }
        auth.currentUser?.reload()?.await()
        auth.currentUser?.let { authController.fetchUserDataAndUpdateUser(it.uid) }

        navController.navigate(screen) { launchSingleTop = true }
    }

    private suspend fun processEmailVerificationAndChangeLink(obbCode: String) {
        val screen = when (authController.applyOobCode(obbCode)) {
            true -> {
                authController.signOut()
                AuthScreens.SignIn(case = SignInCase.AfterEmailChange.name)
            }
            false -> AuthScreens.EmailVerificationFailed
        }

        navController.navigate(screen) { launchSingleTop = true }
    }

    private fun processResetPasswordLink(oobCode: String) {
        navController.navigate(AuthScreens.ResetPassword(oobCode)) { launchSingleTop = true }
    }


    private fun initializeFirebaseDebugger() {
        if (BuildConfig.DEBUG) {
            val firestore: FirebaseFirestore by inject()
            firestore.useEmulator("10.0.2.2", 8080)
            auth.useEmulator("10.0.2.2", 9099)
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
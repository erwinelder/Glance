package com.ataglance.walletglance.core.presentation

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckTokenValidityUseCase
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.GlanciAppComponent
import com.ataglance.walletglance.core.utils.extractOobCode
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavHostController
    private lateinit var billingSubscriptionManager: BillingSubscriptionManager

//    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
//        setupSplashScreen()

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

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides this) {
                navController = rememberNavController()
//                appViewModel = koinViewModel<AppViewModel>()

                LaunchedEffect(true) {
                    navController.currentBackStackEntryFlow.first()
                    handleDeepLink(intent)
                }
                LaunchedEffect(true) {
                    checkTokenValidity()
                }

                GlanciAppComponent(
                    navController = navController
                )
            }
        }
    }

    /*private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.appConfiguration.value.appTheme == null
            }
        }
    }*/

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val uri = intent.data ?: return

        when (val mode = uri.getQueryParameter("mode")) {
            "verifyEmail" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()?.let(::processVerifyEmailLink)
                        ?: Log.e("Email verification link", "No oobCode found in the deep link")
                }
            }
            "verifyAndChangeEmail" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()?.let(::processVerifyAndChangeEmailLink)
                        ?: Log.e("Email verification link", "No oobCode found in the deep link")
                }
            }
            "resetPassword" -> {
                lifecycleScope.launch {
                    uri.extractOobCode()?.let(::processResetPasswordLink)
                        ?: Log.e("Reset password link", "No oobCode found in the deep link")
                }
            }
            else -> Log.e("Deep link", "Unknown deep link mode or action: $mode")
        }
    }

    private fun processVerifyEmailLink(oobCode: String) {
        navController.navigate(AuthScreens.FinishSignUp(oobCode = oobCode)) {
            launchSingleTop = true
        }
    }
    private fun processVerifyAndChangeEmailLink(obbCode: String) {
        navController.navigate(AuthScreens.VerifyEmailUpdate(oobCode = obbCode)) {
            launchSingleTop = true
        }
    }
    private fun processResetPasswordLink(oobCode: String) {
        navController.navigate(AuthScreens.ResetPassword(obbCode = oobCode)) {
            launchSingleTop = true
        }
    }


    private suspend fun checkTokenValidity() {
        val checkTokenValidityUseCase: CheckTokenValidityUseCase = GlobalContext.get().get()
        val result = checkTokenValidityUseCase.execute()

        if (result !is ResultData.Error) return

        when (result.error) {
            AuthError.AppUpdateRequired -> {
                navController.navigate(MainScreens.UpdateRequest) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
            AuthError.SessionExpired -> {
                navController.navigate(AuthScreens.SignIn()) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
            else -> {
                Log.e("MainActivity", "Error checking token validity: ${result.error}")
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
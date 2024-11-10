package com.ataglance.walletglance.auth.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.auth.domain.AuthController
import com.ataglance.walletglance.auth.presentation.screen.SignInScreen
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreen
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    subscriptionViewModel: SubscriptionViewModel,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
) {
    navigation<SettingsScreens.Auth>(startDestination = AuthScreens.SignIn) {
        composable<AuthScreens.SignIn> { backStack ->
            val coroutineScope = rememberCoroutineScope()

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController
            )

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val signInIsAllowed by viewModel.signInIsAllowed.collectAsStateWithLifecycle()

            SignInScreen(
                email = emailState.fieldText,
                onEmailChange = viewModel::updateEmail,
                password = passwordState.fieldText,
                onPasswordChange = viewModel::updatePassword,
                signInIsAllowed = signInIsAllowed,
                onSignInWithEmailAndPassword = { email, password ->
                    coroutineScope.launch {
                        val userRemotePreferences = authController.signIn(email, password)
                            ?: return@launch
                        val userId = authController.getUserId() ?: return@launch

                        appViewModel.setUserId(userId)
                        appViewModel.updatePreferencesAfterSignIn(userRemotePreferences)

                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = SettingsScreens.Accounts
                            )
                        }
                    }
                },
                onNavigateToSignUpScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = AuthScreens.SignInSuccessful
                    )
                }
            )
        }
        composable<AuthScreens.SignUp> { backStack ->
            val coroutineScope = rememberCoroutineScope()

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController
            )

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val signUpIsAllowed by viewModel.signUpIsAllowed.collectAsStateWithLifecycle()

            SignUpScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateEmail,
                passwordState = passwordState,
                onPasswordChange = viewModel::updatePassword,
                signUpIsAllowed = signUpIsAllowed,
                onCreateNewUserWithEmailAndPassword = { email, password ->
                    coroutineScope.launch {
                        val signingUpResult = authController.createNewUser(
                            email = email,
                            password = password,
                            lang = appUiSettings.langCode
                        )
                        if (!signingUpResult) {
                            return@launch
                        }

                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = SettingsScreens.Accounts
                            )
                        }
                    }
                },
                onNavigateToSignInScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = AuthScreens.SignUpSuccessful
                    )
                }
            )
        }
        composable<AuthScreens.SignInSuccessful> {  }
        composable<AuthScreens.SignUpSuccessful> {  }
    }
}
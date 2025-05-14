package com.ataglance.walletglance.auth.presentation.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.presentation.model.AuthResultSuccessScreenState
import com.ataglance.walletglance.auth.presentation.screen.DeleteAccountScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.EmailVerificationErrorScreen
import com.ataglance.walletglance.auth.presentation.screen.EmailVerificationScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.FinishSignUpScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.SignInScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.UpdateEmailScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreenWrapper
import com.ataglance.walletglance.auth.presentation.utils.getAuthNavGraphStartDestination
import com.ataglance.walletglance.billing.presentation.screen.SubscriptionsScreenWrapper
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.errorHandling.presentation.screen.AuthResultSuccessScreen
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Auth>(
        startDestination = AuthScreens.SignIn(case = SignInCase.Default.name)
    ) {
        composable<AuthScreens.SignIn> { backStack ->
            SignInScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration,
                backStack = backStack
            )
        }
        composable<AuthScreens.SignUp> { backStack ->
            SignUpScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.EmailVerification> { backStack ->
            EmailVerificationScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.FinishSignUp> { backStack ->
            FinishSignUpScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.Profile> {
            ProfileScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                authController = authController
            )
        }
        composable<AuthScreens.UpdateEmail> { backStack ->
            UpdateEmailScreenWrapper(
                navController = navController,
                authController = authController,
                backStack = backStack
            )
        }
        composable<AuthScreens.UpdatePassword> { backStack ->
            UpdatePasswordScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                authController = authController,
                backStack = backStack
            )
        }
        composable<AuthScreens.RequestPasswordReset> { backStack ->
            RequestPasswordResetScreenWrapper(
                navController = navController,
                authController = authController,
                backStack = backStack
            )
        }
        composable<AuthScreens.ResetPassword> { backStack ->
            ResetPasswordScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                authController = authController,
                backStack = backStack
            )
        }
        composable<AuthScreens.DeleteAccount> { backStack ->
            DeleteAccountScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                authController = authController,
                backStack = backStack
            )
        }
        composable<AuthScreens.EmailVerificationFailed> {
            EmailVerificationErrorScreen(
                onContinueButtonClick = {
                    navViewModel.popBackStackAndNavigate(
                        navController = navController,
                        screen = getAuthNavGraphStartDestination(
                            isSignedIn = authController.isSignedIn(),
                            signInCase = SignInCase.EmailVerificationError
                        )
                    )
                }
            )
        }
        composable<AuthScreens.ResultSuccess> { backStack ->
            val screenState = AuthResultSuccessScreenState.fromString(
                type = backStack.toRoute<AuthScreens.ResultSuccess>().screenType,
                isAppSetUp = appConfiguration.isSetUp
            )

            val coroutineScope = rememberCoroutineScope()

            AuthResultSuccessScreen(
                screenState = screenState,
                onContinueButtonClick = {
                    coroutineScope.launch {
                        if (screenState.type == AuthResultSuccessScreenType.AccountDeletion) {
                            authController.resetUser()
                            authController.deleteAllLocalData()
                        }
                        navViewModel.popBackStackAndNavigate(
                            navController = navController,
                            screen = screenState.getNextScreenNavigateTo()
                        )
                    }
                }
            )
        }
        composable<AuthScreens.ManageSubscriptions> { backStack ->
            SubscriptionsScreenWrapper(navController = navController, backStack = backStack)
        }
    }
}
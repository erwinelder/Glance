package com.ataglance.walletglance.auth.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.presentation.screen.DeleteAccountScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.EmailUpdateEmailVerificationScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.FinishSignUpScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.RequestEmailUpdateScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.SignInScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.SignUpEmailVerificationScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreenWrapper
import com.ataglance.walletglance.auth.presentation.screen.VerifyEmailUpdateScreenWrapper
import com.ataglance.walletglance.billing.presentation.screen.SubscriptionsScreenWrapper
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

fun NavGraphBuilder.authGraph(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Auth>(
        startDestination = AuthScreens.SignIn()
    ) {
        composable<AuthScreens.SignIn> { backStack ->
            SignInScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration,
                backStack = backStack
            )
        }
        composable<AuthScreens.SignUp> { backStack ->
            SignUpScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.SignUpEmailVerification> { backStack ->
            SignUpEmailVerificationScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.FinishSignUp> { backStack ->
            FinishSignUpScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.Profile> {
            ProfileScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.UpdateEmail> { backStack ->
            RequestEmailUpdateScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.EmailUpdateEmailVerification> { backStack ->
            EmailUpdateEmailVerificationScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.VerifyEmailUpdate> { backStack ->
            VerifyEmailUpdateScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.UpdatePassword> { backStack ->
            UpdatePasswordScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.RequestPasswordReset> { backStack ->
            RequestPasswordResetScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                backStack = backStack
            )
        }
        composable<AuthScreens.ResetPassword> { backStack ->
            ResetPasswordScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration,
                backStack = backStack
            )
        }
        composable<AuthScreens.DeleteAccount> { backStack ->
            DeleteAccountScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.ManageSubscriptions> { backStack ->
            SubscriptionsScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                backStack = backStack
            )
        }
    }
}
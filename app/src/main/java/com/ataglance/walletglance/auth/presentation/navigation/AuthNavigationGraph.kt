package com.ataglance.walletglance.auth.presentation.navigation

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
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Auth>(
        startDestination = AuthScreens.SignIn()
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
        composable<AuthScreens.SignUpEmailVerification> { backStack ->
            SignUpEmailVerificationScreenWrapper(
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
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.UpdateEmail> { backStack ->
            RequestEmailUpdateScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.EmailUpdateEmailVerification> { backStack ->
            EmailUpdateEmailVerificationScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.VerifyEmailUpdate> { backStack ->
            VerifyEmailUpdateScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                backStack = backStack
            )
        }
        composable<AuthScreens.UpdatePassword> { backStack ->
            UpdatePasswordScreenWrapper(
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.RequestPasswordReset> { backStack ->
            RequestPasswordResetScreenWrapper(
                navController = navController,
                backStack = backStack
            )
        }
        composable<AuthScreens.ResetPassword> { backStack ->
            ResetPasswordScreenWrapper(
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration,
                backStack = backStack
            )
        }
        composable<AuthScreens.DeleteAccount> { backStack ->
            DeleteAccountScreenWrapper(
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AuthScreens.ManageSubscriptions> { backStack ->
            SubscriptionsScreenWrapper(navController = navController, backStack = backStack)
        }
    }
}
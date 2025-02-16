package com.ataglance.walletglance.auth.presentation.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ataglance.walletglance.auth.domain.mapper.toAuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.model.AuthResultSuccessScreenState
import com.ataglance.walletglance.auth.presentation.screen.DeleteAccountScreen
import com.ataglance.walletglance.auth.presentation.screen.EmailVerificationErrorScreen
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreen
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreen
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreen
import com.ataglance.walletglance.auth.presentation.screen.SignInScreen
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreen
import com.ataglance.walletglance.auth.presentation.screen.UpdateEmailScreen
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreen
import com.ataglance.walletglance.auth.presentation.utils.getAuthNavGraphStartDestination
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.billing.presentation.screen.SubscriptionsScreen
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.core.utils.takeActionIf
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.mapper.toUiState
import com.ataglance.walletglance.errorHandling.presentation.screen.AuthResultSuccessScreen
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
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
            val case = SignInCase.valueOf(backStack.toRoute<AuthScreens.SignIn>().case)

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val signInIsAllowed by viewModel.signInIsAllowed.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            SignInScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateAndValidateEmail,
                passwordState = passwordState,
                onPasswordChange = viewModel::updateAndValidatePassword,
                signInIsAllowed = signInIsAllowed,
                onSignIn = {
                    coroutineScope.launch {
                        val result = authController.signIn(
                            email = emailState.fieldText, password = passwordState.fieldText
                        )
                        when (result) {
                            is Result.Success -> {
                                if (result.success == null) {
                                    navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                                        navController = navController,
                                        screenType = case.toAuthResultSuccessScreenType()
                                    )
                                } else {
                                    viewModel.setResultState(result.success.toUiState())
                                }
                            }
                            is Result.Error -> viewModel.setResultState(result.error.toUiState())
                        }
                    }
                },
                onNavigateToRequestPasswordResetScreen = {
                    navViewModel.navigateToScreen(navController, AuthScreens.RequestPasswordReset)
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState,
                onNavigateToSignUpScreen = takeActionIf(case != SignInCase.AfterEmailChange) {
                    navViewModel.popBackStackAndNavigateToScreen(navController, AuthScreens.SignUp)
                },
                onContinueAsGuest = takeActionIf(case == SignInCase.Default && !appConfiguration.isSetUp) {
                    navViewModel.popBackStackAndNavigateToScreen(
                        navController = navController, screen = SettingsScreens.Accounts
                    )
                }
            )
        }
        composable<AuthScreens.SignUp> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val coroutineScope = rememberCoroutineScope()

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val passwordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val confirmPasswordState by viewModel.newPasswordConfirmationState.collectAsStateWithLifecycle()
            val signUpIsAllowed by viewModel.signUpIsAllowed.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            SignUpScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateAndValidateEmail,
                passwordState = passwordState,
                onPasswordChange = viewModel::updateAndValidateNewPassword,
                confirmPasswordState = confirmPasswordState,
                onConfirmPasswordChange = viewModel::updateAndValidateNewPasswordConfirmation,
                signUpIsAllowed = signUpIsAllowed,
                onCreateNewUserWithEmailAndPassword = { email, password ->
                    coroutineScope.launch {
                        val result = authController.createNewUser(
                            email = email,
                            password = password,
                            appLanguageCode = appConfiguration.langCode
                        )
                        viewModel.setResultState(result.toUiState())
                    }
                },
                onNavigateToSignInScreen = {
                    navViewModel.popBackStackAndNavigateToScreen(
                        navController = navController,
                        screen = AuthScreens.SignIn(SignInCase.Default.name)
                    )
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.Profile> {
            val coroutineScope = rememberCoroutineScope()

            ProfileScreen(
                onNavigateBack = navController::popBackStack,
                onSignOut = {
                    coroutineScope.launch {
                        authController.signOut()
                        navController.popBackStack()
                    }
                },
                onNavigateToScreen = { screen ->
                    navViewModel.navigateToScreen(navController, screen)
                },
                onPopBackStackAndNavigateToScreen = { screen ->
                    navViewModel.popBackStackAndNavigateToScreen(navController, screen)
                }
            )
        }
        composable<AuthScreens.UpdateEmail> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val newEmailState by viewModel.newEmailState.collectAsStateWithLifecycle()
            val emailUpdateIsAllowed by viewModel.emailUpdateIsAllowed.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            UpdateEmailScreen(
                passwordState = passwordState,
                onPasswordChange = viewModel::updateAndValidatePassword,
                newEmailState = newEmailState,
                onNewEmailChange = viewModel::updateAndValidateNewEmail,
                emailUpdateIsAllowed = emailUpdateIsAllowed,
                onUpdateEmailButtonClick = {
                    coroutineScope.launch {
                        val result = authController.requestEmailUpdate(
                            password = passwordState.fieldText, newEmail = newEmailState.fieldText
                        )
                        viewModel.setResultState(result.toUiState())
                    }
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.UpdatePassword> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
                .collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            UpdatePasswordScreen(
                currentPasswordState = currentPasswordState,
                onCurrentPasswordChange = viewModel::updateAndValidatePassword,
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateAndValidateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateAndValidateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    coroutineScope.launch {
                        val result = authController.updatePassword(
                            currentPassword = currentPasswordState.fieldText,
                            newPassword = newPasswordState.fieldText
                        )
                        when (result) {
                            is Result.Success -> {
                                navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                                    navController = navController,
                                    screenType = AuthResultSuccessScreenType.PasswordUpdate
                                )
                            }
                            is Result.Error -> viewModel.setResultState(result.toUiState())
                        }
                    }
                },
                onNavigateToRequestPasswordResetScreen = {
                    navViewModel.popBackStackAndNavigateToScreen(
                        navController = navController,
                        screen = AuthScreens.RequestPasswordReset
                    )
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.RequestPasswordReset> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val emailIsValid by viewModel.emailIsValid.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            RequestPasswordResetScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateAndValidateEmail,
                requestIsAllowed = emailIsValid,
                onRequestPasswordResetButtonClick = {
                    coroutineScope.launch {
                        val result = authController.requestPasswordReset(email = emailState.fieldText)
                        viewModel.setResultState(result.toUiState())
                    }
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.ResetPassword> { backStack ->
            val obbCode = backStack.toRoute<AuthScreens.ResetPassword>().obbCode

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }
            LaunchedEffect(obbCode) {
                viewModel.setObbCode(obbCode)
            }

            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordResetIsAllowed
                .collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            ResetPasswordScreen(
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateAndValidateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateAndValidateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    coroutineScope.launch {
                        val result = authController.setNewPassword(
                            obbCode = obbCode, newPassword = newPasswordState.fieldText
                        )
                        when (result) {
                            is Result.Success -> {
                                navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                                    navController = navController,
                                    screenType = AuthResultSuccessScreenType.PasswordUpdate
                                )
                            }
                            is Result.Error -> viewModel.setResultState(result.toUiState())
                        }
                    }
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.DeleteAccount> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(email = authController.getEmail())
            )
            LaunchedEffect(true) {
                viewModel.resetAllFieldsExceptEmail()
            }

            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val deletionIsAllowed by viewModel.signInIsAllowed.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            DeleteAccountScreen(
                passwordState = passwordState,
                onPasswordChange = viewModel::updateAndValidatePassword,
                deletionIsAllowed = deletionIsAllowed,
                onDeleteAccount = {
                    coroutineScope.launch {
                        val result = authController.deleteAccount(
                            password = passwordState.fieldText
                        )

                        when (result) {
                            is Result.Success -> {
                                navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                                    navController = navController,
                                    screenType = AuthResultSuccessScreenType.AccountDeletion
                                )
                            }
                            is Result.Error -> {
                                viewModel.setResultState(result.toUiState())
                            }
                        }
                    }
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.EmailVerificationFailed> {
            EmailVerificationErrorScreen(
                onContinueButtonClick = {
                    navViewModel.popBackStackAndNavigateToScreen(
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
                        navViewModel.popBackStackAndNavigateToScreen(
                            navController = navController,
                            screen = screenState.getNextScreenNavigateTo()
                        )
                    }
                }
            )
        }
        composable<AuthScreens.ManageSubscriptions> { backStack ->
            val viewModel = backStack.sharedKoinNavViewModel<SubscriptionViewModel>(navController)

            val activeSubscriptions by viewModel.activeSubscriptions.collectAsStateWithLifecycle()
            val availableSubscriptions by viewModel.availableSubscriptions.collectAsStateWithLifecycle()
            val purchaseResult by viewModel.purchaseResult.collectAsStateWithLifecycle()

            val activity = LocalActivity.current

            SubscriptionsScreen(
                onNavigateBack = navController::popBackStack,
                activeSubscriptions = activeSubscriptions,
                availableSubscriptions = availableSubscriptions,
                onStartPurchase = { subscription ->
                    activity?.let {
                        viewModel.startPurchase(activity = it, subscription = subscription)
                    }
                },
                purchaseResultUiState = purchaseResult,
                onResultReset = viewModel::resetPurchaseResult
            )
        }
    }
}
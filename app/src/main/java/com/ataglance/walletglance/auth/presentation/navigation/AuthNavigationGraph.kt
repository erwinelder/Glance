package com.ataglance.walletglance.auth.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthSuccessfulScreenType
import com.ataglance.walletglance.auth.domain.model.AuthSuccessfulScreenTypeEnum
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.screen.AuthSuccessfulScreen
import com.ataglance.walletglance.auth.presentation.screen.EmailVerificationErrorScreen
import com.ataglance.walletglance.auth.presentation.screen.PasswordResetSuccessfulScreen
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreen
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreen
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreen
import com.ataglance.walletglance.auth.presentation.screen.SignInScreen
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreen
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreen
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.errorHandling.mapper.toUiState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    subscriptionViewModel: SubscriptionViewModel,
    appViewModel: AppViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Auth>(startDestination = AuthScreens.SignIn(SignInCase.Default)) {
        composable<AuthScreens.SignIn> { backStack ->
            val case = backStack.toRoute<AuthScreens.SignIn>().case

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            val coroutineScope = rememberCoroutineScope()

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
                        when (val result = authController.signIn(email, password)) {
                            is ResultData.Success -> {
                                appViewModel.updatePreferencesAfterSignIn(result.data)
                                navViewModel.navigateToScreenMovingTowardsLeft(
                                    navController = navController,
                                    screen = AuthScreens.AuthSuccessful(
                                        screenType = if (case == SignInCase.Default) {
                                            AuthSuccessfulScreenTypeEnum.AfterSignIn
                                        } else {
                                            AuthSuccessfulScreenTypeEnum.AfterSignUp
                                        }.name
                                    )
                                )
                            }
                            is ResultData.Error -> {
                                if (result.error == AuthError.EmailVerificationError) {
                                    val verificationEmailResult = authController.sendSignUpVerificationEmail()
                                    viewModel.setResultState(verificationEmailResult.toUiState())
                                    if (verificationEmailResult is Result.Success) {
                                        authController.getUserId()?.let {
                                            appViewModel.setUserId(it)
                                        }
                                    }
                                } else {
                                    viewModel.setResultState(result.toUiState())
                                }
                            }
                        }
                    }
                },
                onNavigateToSignUpScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = AuthScreens.SignUp
                    )
                }
            )
        }
        composable<AuthScreens.SignUp> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            val coroutineScope = rememberCoroutineScope()

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val confirmPasswordState by viewModel.confirmPasswordState.collectAsStateWithLifecycle()
            val signUpIsAllowed by viewModel.signUpIsAllowed.collectAsStateWithLifecycle()

            SignUpScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateEmail,
                passwordState = passwordState,
                onPasswordChange = viewModel::updatePassword,
                confirmPasswordState = confirmPasswordState,
                onConfirmPasswordChange = viewModel::updateConfirmPassword,
                signUpIsAllowed = signUpIsAllowed,
                onCreateNewUserWithEmailAndPassword = { email, password ->
                    coroutineScope.launch {
                        val userCreationResult = authController.createNewUser(
                            email = email,
                            password = password,
                            lang = appConfiguration.langCode
                        )
                        if (userCreationResult is ResultData.Error) {
                            viewModel.setResultState(userCreationResult.toUiState())
                            return@launch
                        }

                        val verificationEmailResult = authController.sendSignUpVerificationEmail()
                        viewModel.setResultState(verificationEmailResult.toUiState())
                        if (verificationEmailResult is Result.Success) {
                            appViewModel.setUserId((userCreationResult as ResultData.Success).data)
                        }
                    }
                },
                onNavigateToSignInScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = AuthScreens.SignIn(SignInCase.Default)
                    )
                }
            )
        }
        composable<AuthScreens.EmailVerificationFailed> {
            EmailVerificationErrorScreen {
                navViewModel.navigateToScreenMovingTowardsLeft(
                    navController = navController,
                    screen = AuthScreens.SignIn(SignInCase.EmailVerificationError)
                )
            }
        }
        composable<AuthScreens.AuthSuccessful> { backStack ->
            val screenType = AuthSuccessfulScreenType.fromString(
                backStack.toRoute<AuthScreens.AuthSuccessful>().screenType
            )

            AuthSuccessfulScreen(
                screenType = screenType,
                onContinueButtonClick = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = when (screenType.type) {
                            AuthSuccessfulScreenTypeEnum.AfterSignIn -> MainScreens.FinishSetup
                            AuthSuccessfulScreenTypeEnum.AfterSignUp -> SettingsScreens.Accounts
                        }
                    )
                }
            )
        }
        composable<AuthScreens.Profile> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            ProfileScreen()
        }
        composable<AuthScreens.UpdatePassword> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            val coroutineScope = rememberCoroutineScope()

            val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
                .collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            UpdatePasswordScreen(
                currentPasswordState = currentPasswordState,
                onCurrentPasswordChange = viewModel::updatePassword,
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    coroutineScope.launch {
                        val result = authController.updatePassword(
                            currentPassword = currentPasswordState.fieldText,
                            newPassword = newPasswordState.fieldText
                        )
                        viewModel.setResultState(result.toUiState())
                    }
                },
                onNavigateToRequestPasswordResetScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
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
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            val coroutineScope = rememberCoroutineScope()

            val emailState by viewModel.emailState.collectAsStateWithLifecycle()
            val emailIsValid by viewModel.emailIsValid.collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            RequestPasswordResetScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateEmail,
                requestIsAllowed = emailIsValid,
                onRequestPasswordResetButtonClick = {
                    coroutineScope.launch {
                        val result = authController.requestPasswordReset(emailState.fieldText)
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
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            LaunchedEffect(obbCode) {
                viewModel.setObbCode(obbCode)
            }

            val coroutineScope = rememberCoroutineScope()

            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
                .collectAsStateWithLifecycle()
            val resultState by viewModel.resultState.collectAsStateWithLifecycle()

            ResetPasswordScreen(
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    coroutineScope.launch {
                        val result = authController.setNewPassword(
                            obbCode = obbCode, newPassword = newPasswordState.fieldText
                        )
                        viewModel.setResultState(result.toUiState())
                    }
                },
                resultState = resultState,
                onResultReset = viewModel::resetResultState
            )
        }
        composable<AuthScreens.PasswordResetSuccessful> {
            PasswordResetSuccessfulScreen(
                isAppSetUp = appConfiguration.isSetUp,
                onContinueButtonClick = {
                    navController.popBackStack(
                        route = if (appConfiguration.isSetUp) AuthScreens.Profile else
                            AuthScreens.SignIn,
                        inclusive = false
                    )
                }
            )
        }
    }
}
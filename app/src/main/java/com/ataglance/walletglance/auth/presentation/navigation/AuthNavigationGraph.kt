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
import com.ataglance.walletglance.auth.domain.model.AuthenticationSuccessfulScreenType
import com.ataglance.walletglance.auth.domain.model.ProfileScreenTypeEnum
import com.ataglance.walletglance.auth.presentation.screen.AuthSuccessfulScreen
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
    navigation<SettingsScreens.Auth>(startDestination = AuthScreens.SignIn) {
        composable<AuthScreens.SignIn> { backStack ->
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
                                val userId = authController.getUserId() ?: return@launch

                                appViewModel.setUserId(userId)
                                appViewModel.updatePreferencesAfterSignIn(result.data)

                                navViewModel.navigateToScreenMovingTowardsLeft(
                                    navController = navController,
                                    screen = AuthScreens.AuthSuccessful(
                                        screenType = ProfileScreenTypeEnum.AfterSignIn.name
                                    )
                                )
                            }
                            is ResultData.Error -> {
                                viewModel.setResultState(result.toUiState())
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
                        val result = authController.createNewUser(
                            email = email,
                            password = password,
                            lang = appConfiguration.langCode
                        )

                        when (result) {
                            is ResultData.Success -> {
                                appViewModel.setUserId(result.data)
                                navViewModel.navigateToScreenMovingTowardsLeft(
                                    navController = navController,
                                    screen = AuthScreens.AuthSuccessful(
                                        screenType = ProfileScreenTypeEnum.AfterSignUp.name
                                    )
                                )
                            }
                            is ResultData.Error -> {
                                viewModel.setResultState(result.toUiState())
                            }
                        }
                    }
                },
                onNavigateToSignInScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = AuthScreens.SignIn
                    )
                }
            )
        }
        composable<AuthScreens.AuthSuccessful> { backStack ->
            val screenType = AuthenticationSuccessfulScreenType.fromString(
                backStack.toRoute<AuthScreens.AuthSuccessful>().screenType
            )

            AuthSuccessfulScreen(
                screenType = screenType,
                onContinueButtonClick = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = when (screenType.type) {
                            ProfileScreenTypeEnum.AfterSignIn -> MainScreens.FinishSetup
                            ProfileScreenTypeEnum.AfterSignUp -> SettingsScreens.Accounts
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
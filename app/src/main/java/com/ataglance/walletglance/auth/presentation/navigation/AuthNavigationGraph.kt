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
import com.ataglance.walletglance.errorHandling.domain.mapper.toTaskResult
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    subscriptionViewModel: SubscriptionViewModel,
    appViewModel: AppViewModel,
    appConfiguration: AppConfiguration,
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
                        val result = authController.signIn(email, password)

                        if (result.isSuccessful) {
                            val userId = authController.getUserId() ?: return@launch

                            appViewModel.setUserId(userId)
                            result.data?.let { appViewModel.updatePreferencesAfterSignIn(it) }

                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = AuthScreens.AuthenticationSuccessful(
                                    screenType = ProfileScreenTypeEnum.AfterSignIn.name
                                )
                            )
                        } else {
                            viewModel.setTaskResult(result.toTaskResult())
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

                        if (result.isSuccessful) {
                            result.data?.let { appViewModel.setUserId(it) }
                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = AuthScreens.AuthenticationSuccessful(
                                    screenType = ProfileScreenTypeEnum.AfterSignUp.name
                                )
                            )
                        } else {
                            viewModel.setTaskResult(result.toTaskResult())
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
        composable<AuthScreens.AuthenticationSuccessful> { backStack ->
            val screenType = AuthenticationSuccessfulScreenType.fromString(
                backStack.toRoute<AuthScreens.AuthenticationSuccessful>().screenType
            )

            AuthSuccessfulScreen(
                screenType = screenType,
                onContinueButtonClick = {
                    when (screenType.type) {
                        ProfileScreenTypeEnum.AfterSignIn -> {
                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = MainScreens.FinishSetup
                            )
                        }
                        ProfileScreenTypeEnum.AfterSignUp -> {
                            navViewModel.navigateToScreenMovingTowardsLeft(
                                navController = navController,
                                screen = SettingsScreens.Start
                            )
                        }
                    }
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
            val taskResult by viewModel.taskResult.collectAsStateWithLifecycle()

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
                        viewModel.setTaskResult(result)
                    }
                },
                taskResult = taskResult,
                onTaskResultReset = viewModel::resetTaskResult
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
            val taskResult by viewModel.taskResult.collectAsStateWithLifecycle()

            RequestPasswordResetScreen(
                emailState = emailState,
                onEmailChange = viewModel::updateEmail,
                requestIsAllowed = emailIsValid,
                onRequestPasswordResetButtonClick = {
                    coroutineScope.launch {
                        val result = authController.requestPasswordReset(emailState.fieldText)
                        viewModel.setTaskResult(result)
                    }
                },
                taskResult = taskResult,
                onTaskResultReset = viewModel::resetTaskResult
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
            val taskResult by viewModel.taskResult.collectAsStateWithLifecycle()

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
                        viewModel.setTaskResult(result)
                    }
                },
                taskResult = taskResult,
                onTaskResultReset = viewModel::resetTaskResult
            )
        }
    }
}
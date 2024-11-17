package com.ataglance.walletglance.auth.presentation.navigation

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
            val coroutineScope = rememberCoroutineScope()

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
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

                        navViewModel.navigateToScreenMovingTowardsLeft(
                            navController = navController,
                            screen = AuthScreens.AuthenticationSuccessful(
                                screenType = ProfileScreenTypeEnum.AfterSignIn.name
                            )
                        )
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
            val coroutineScope = rememberCoroutineScope()

            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

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
                        val userId = authController.createNewUser(
                            email = email,
                            password = password,
                            lang = appConfiguration.langCode
                        ) ?: return@launch

                        appViewModel.setUserId(userId)

                        navViewModel.navigateToScreenMovingTowardsLeft(
                            navController = navController,
                            screen = AuthScreens.AuthenticationSuccessful(
                                screenType = ProfileScreenTypeEnum.AfterSignUp.name
                            )
                        )
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

            val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
                .collectAsStateWithLifecycle()

            UpdatePasswordScreen(
                currentPasswordState = currentPasswordState,
                onCurrentPasswordChange = viewModel::updatePassword,
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    authController
                }
            )
        }
        composable<AuthScreens.ResetPassword> { backStack ->
            val viewModel = backStack.sharedViewModel<AuthViewModel>(
                navController = navController,
                factory = AuthViewModelFactory(
                    email = authController.getEmail()
                )
            )

            val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
            val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
                .collectAsStateWithLifecycle()
            val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
                .collectAsStateWithLifecycle()

            ResetPasswordScreen(
                newPasswordState = newPasswordState,
                onNewPasswordChange = viewModel::updateNewPassword,
                newPasswordConfirmationState = newPasswordConfirmationState,
                onNewPasswordConfirmationChange = viewModel::updateNewPasswordConfirmation,
                passwordUpdateIsAllowed = passwordUpdateIsAllowed,
                onUpdatePasswordButtonClick = {
                    authController
                }
            )
        }
    }
}
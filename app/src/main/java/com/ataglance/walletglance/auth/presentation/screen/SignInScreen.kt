package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.container.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.utils.takeActionIf
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignInScreenWrapper(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<SignInViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.SignIn>().email
        )
    }

    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val signInIsAllowed by viewModel.signInIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    SignInScreen(
        emailState = emailState,
        onEmailChange = viewModel::updateAndValidateEmail,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        signInIsAllowed = signInIsAllowed,
        onSignIn = viewModel::signIn,
        onNavigateToRequestPasswordResetScreen = {
            navViewModel.navigateToScreen(
                navController = navController,
                screen = AuthScreens.RequestPasswordReset(email = emailState.fieldText)
            )
        },
        onNavigateToSignUpScreen = {
            navViewModel.popBackStackAndNavigate(
                navController = navController,
                screen = AuthScreens.SignUp(email = emailState.fieldText)
            )
        },
        onContinueAsGuest = takeActionIf(!appConfiguration.isSetUp) {
            navViewModel.popBackStackAndNavigate(
                navController = navController, screen = SettingsScreens.Accounts
            )
        },
        requestState = requestState,
        onCancelRequest = viewModel::cancelSignIn,
        onSuccessClose = {
            if (appConfiguration.isSetUp) {
                navViewModel.navigateToScreenPoppingToStartDestination(
                    navController = navController, screenToNavigateTo = AuthScreens.Profile
                )
            } else {
                navViewModel.navigateAndPopUpTo(
                    navController = navController, screenToNavigateTo = MainScreens.FinishSetup
                )
            }
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun SignInScreen(
    screenPadding: PaddingValues = PaddingValues(0.dp),
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignIn: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    onNavigateToSignUpScreen: (() -> Unit)?,
    onContinueAsGuest: (() -> Unit)?,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    if (requestState != null) {
        SetBackHandler()
    }

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessClose = onSuccessClose,
        onErrorClose = onErrorClose
    ) {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.sign_in_to_your_account),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    emailState = emailState,
                    onEmailChange = onEmailChange,
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    onSignIn = onSignIn
                )
            },
            buttonBlockUnderGlassSurface = { keyboardInFocus ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PrimaryButton(
                        text = stringResource(R.string.sign_in),
                        enabled = signInIsAllowed,
                        onClick = onSignIn
                    )
                    if (onNavigateToSignUpScreen != null) {
                        KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                            SecondaryButton(
                                text = stringResource(R.string.reset_password),
                                onClick = onNavigateToRequestPasswordResetScreen
                            )
                        }
                    }
                }
            },
            bottomButtonBlock = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (onNavigateToSignUpScreen != null) {
                        SecondaryButton(
                            text = stringResource(R.string.sign_up),
                            onClick = onNavigateToSignUpScreen
                        )
                    }
                    if (onNavigateToSignUpScreen != null && onContinueAsGuest != null) {
                        SecondaryButton(
                            text = stringResource(R.string.continue_as_guest),
                            onClick = onContinueAsGuest
                        )
                    }
                    if (onNavigateToSignUpScreen == null) {
                        SecondaryButton(
                            text = stringResource(R.string.reset_password),
                            onClick = onNavigateToRequestPasswordResetScreen
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = emailState,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email),
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onSignIn
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignInScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val email = "example@domain.com"
    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignInScreen(
            emailState = ValidatedFieldState(
                fieldText = email,
                validationStates = UserDataValidator.validateEmail(email).toUiStates()
            ),
            onEmailChange = {},
            passwordState = ValidatedFieldState(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password)
                    .toUiStates()
            ),
            onPasswordChange = {},
            signInIsAllowed = UserDataValidator.isValidEmail(email) &&
                    UserDataValidator.isValidPassword(password),
            onSignIn = {},
            onNavigateToRequestPasswordResetScreen = {},
            onNavigateToSignUpScreen = {},
            onContinueAsGuest = {},
            requestState = null,
//            requestState = RequestState.Loading(messageRes = R.string.verifying_your_credentials_loader),
//            requestState = RequestState.Result(resultState = AuthSuccess.SignedIn.toResultWithButtonState()),
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}

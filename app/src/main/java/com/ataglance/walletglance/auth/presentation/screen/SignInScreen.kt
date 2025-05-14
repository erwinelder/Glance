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
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.container.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.utils.takeActionIf
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
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
    val case = SignInCase.valueOf(backStack.toRoute<AuthScreens.SignIn>().case)

    val signInViewModel = koinViewModel<SignInViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.SignIn>().email
        )
    }

    val emailState by signInViewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by signInViewModel.passwordState.collectAsStateWithLifecycle()
    val signInIsAllowed by signInViewModel.signInIsAllowed.collectAsStateWithLifecycle()
    val requestState by signInViewModel.requestState.collectAsStateWithLifecycle()

    SignInScreen(
        emailState = emailState,
        onEmailChange = signInViewModel::updateAndValidateEmail,
        passwordState = passwordState,
        onPasswordChange = signInViewModel::updateAndValidatePassword,
        signInIsAllowed = signInIsAllowed,
        onSignIn = signInViewModel::signIn,
        onNavigateToRequestPasswordResetScreen = {
            navViewModel.navigateToScreen(navController, AuthScreens.RequestPasswordReset)
        },
        onNavigateToSignUpScreen = takeActionIf(case != SignInCase.AfterEmailChange) {
            navViewModel.popBackStackAndNavigate(navController, AuthScreens.SignUp)
        },
        onContinueAsGuest = takeActionIf(case == SignInCase.Default && !appConfiguration.isSetUp) {
            navViewModel.popBackStackAndNavigate(
                navController = navController, screen = SettingsScreens.Accounts
            )
        },
        requestState = requestState,
        onCancelRequest = signInViewModel::cancelRequest,
        onSuccessClose = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = if (appConfiguration.isSetUp)
                    AuthScreens.Profile else MainScreens.FinishSetup
            )
        },
        onErrorClose = signInViewModel::resetRequestState
    )
}

@Composable
fun SignInScreen(
    screenPadding: PaddingValues = PaddingValues(0.dp),
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignIn: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    onNavigateToSignUpScreen: (() -> Unit)?,
    onContinueAsGuest: (() -> Unit)?,
    requestState: RequestState?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    SetBackHandler()

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
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
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
            emailState = ValidatedFieldUiState(
                fieldText = email,
                validationStates = UserDataValidator.validateEmail(email).toUiStates()
            ),
            onEmailChange = {},
            passwordState = ValidatedFieldUiState(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotEmpty(password)
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

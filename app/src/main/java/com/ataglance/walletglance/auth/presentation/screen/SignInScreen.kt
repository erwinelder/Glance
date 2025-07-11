package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.SignInViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.takeActionIf
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignInScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
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
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
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
        onSuccessButton = {
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
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun SignInScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignIn: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    onNavigateToSignUpScreen: (() -> Unit)?,
    onContinueAsGuest: (() -> Unit)?,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.User,
        title = stringResource(R.string.sign_in_to_your_account),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.sign_in),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = { isKeyboardVisible ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GlassSurface(
                    filledWidths = FilledWidthByScreenType(compact = .86f),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    GlassSurfaceContent(
                        emailState = emailState,
                        onEmailChange = onEmailChange,
                        passwordState = passwordState,
                        onPasswordChange = onPasswordChange,
                        onSignIn = onSignIn
                    )
                }
                PrimaryButton(
                    text = stringResource(R.string.sign_in),
                    enabled = signInIsAllowed,
                    onClick = onSignIn
                )
                if (onNavigateToSignUpScreen != null) {
                    KeyboardTypingAnimatedVisibilityContainer(isVisible = !isKeyboardVisible) {
                        SecondaryButton(
                            text = stringResource(R.string.reset_password),
                            onClick = onNavigateToRequestPasswordResetScreen
                        )
                    }
                }
            }
        },
        screenBottomContent = {
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
            labelText = stringResource(R.string.email),
            placeholderText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            labelText = stringResource(R.string.password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onSignIn
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO, locale = "en")
@Composable
fun SignInScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val email = "example@domain.com"
    val emailState = ValidatedFieldState(
        fieldText = email,
        validationStates = UserDataValidator.validateEmail(email).toUiStates()
    )
    val password = "_Password1"
    val passwordState = ValidatedFieldState(
        fieldText = password,
        validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password).toUiStates()
    )
    val signInIsAllowed = UserDataValidator.isValidEmail(email) &&
            UserDataValidator.isValidPassword(password)

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.verifying_your_credentials_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignInScreen(
            onNavigateBack = {},
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            signInIsAllowed = signInIsAllowed,
            onSignIn = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.verifying_your_credentials_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.SignedIn.toResultStateButton()
                    )
                }
            },
            onNavigateToRequestPasswordResetScreen = {},
            onNavigateToSignUpScreen = {},
            onContinueAsGuest = {},

            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onSuccessButton = { requestState = initialRequestState },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}

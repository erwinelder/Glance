package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun SignUpScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedKoinNavViewModel<SignUpViewModel>(navController) {
        parametersOf(
            backStack.toRoute<AuthScreens.SignUp>().email
        )
    }

    val nameState by viewModel.nameState.collectAsStateWithLifecycle()
    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val confirmPasswordState by viewModel.confirmPasswordState.collectAsStateWithLifecycle()
    val signUpIsAllowed by viewModel.signUpIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    SignUpScreen(
        screenPadding = screenPadding,
        nameState = nameState,
        onNameChange = viewModel::updateAndValidateName,
        emailState = emailState,
        onEmailChange = viewModel::updateAndValidateEmail,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        confirmPasswordState = confirmPasswordState,
        onConfirmPasswordChange = viewModel::updateAndValidateConfirmPassword,
        signUpIsAllowed = signUpIsAllowed,
        onSignUp = {
            job = coroutineScope.launch {
                if (!viewModel.signUp()) return@launch
                navViewModel.navigateToScreen(
                    navController = navController, screen = AuthScreens.SignUpEmailVerification
                )
            }
        },
        onNavigateToSignInScreen = {
            navViewModel.popBackStackAndNavigate(
                navController = navController,
                screen = AuthScreens.SignIn(email = emailState.fieldText)
            )
        },
        requestState = requestState,
        onCancelRequest = {
            job?.cancel()
            job = null
            viewModel.resetRequestState()
        },
        onErrorClose = viewModel::resetRequestState,
    )
}

@Composable
fun SignUpScreen(
    screenPadding: PaddingValues = PaddingValues(),
    nameState: ValidatedFieldState,
    onNameChange: (String) -> Unit,
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: ValidatedFieldState,
    onConfirmPasswordChange: (String) -> Unit,
    signUpIsAllowed: Boolean,
    onSignUp: () -> Unit,
    onNavigateToSignInScreen: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onErrorClose: () -> Unit
) {
    if (requestState != null) {
        SetBackHandler()
    }

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = onCancelRequest,
        onErrorClose = onErrorClose
    ) {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.create_new_account),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    nameState = nameState,
                    onNameChange = onNameChange,
                    emailState = emailState,
                    onEmailChange = onEmailChange,
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    confirmPasswordState = confirmPasswordState,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onSignUp = onSignUp
                )
            },
            buttonBlockUnderGlassSurface = {
                PrimaryButton(
                    text = stringResource(R.string.sign_up),
                    enabled = signUpIsAllowed,
                    onClick = onSignUp
                )
            },
            bottomButtonBlock = {
                SecondaryButton(
                    text = stringResource(R.string.sign_in),
                    onClick = onNavigateToSignInScreen
                )
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    nameState: ValidatedFieldState,
    onNameChange: (String) -> Unit,
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: ValidatedFieldState,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUp: () -> Unit
) {
    val scrollState = rememberScrollState()

    GlassSurfaceContentColumnWrapper(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        SmallTextFieldWithLabelAndMessages(
            state = nameState,
            onValueChange = onNameChange,
            keyboardType = KeyboardType.Text,
            placeholderText = stringResource(R.string.name),
            labelText = stringResource(R.string.name),
            imeAction = ImeAction.Next
        )
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
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = confirmPasswordState,
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.confirm_password),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onSignUp
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignUpScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val name = "Erwin"
    val email = "example@domain.com"
    val password = "_Password1"
    val confirmPassword = "_Password111111"
    val passwordsMatch = true

    val nameState = ValidatedFieldState(
        fieldText = name,
        validationStates = UserDataValidator.validateName(name).toUiStates()
    )
    val emailState = ValidatedFieldState(
        fieldText = email,
        validationStates = UserDataValidator.validateEmail(email).toUiStates()
    )
    val passwordState = ValidatedFieldState(
        fieldText = password,
        validationStates = UserDataValidator.validatePassword(password).toUiStates()
    )
    val confirmPasswordState = ValidatedFieldState(
        fieldText = confirmPassword,
        validationStates = UserDataValidator
            .validateConfirmationPassword(password, confirmPassword)
            .toUiStates()
    )

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignUpScreen(
            nameState = nameState,
            onNameChange = {},
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            confirmPasswordState = confirmPasswordState,
            onConfirmPasswordChange = {},
            signUpIsAllowed = UserDataValidator.isValidName(name) &&
                    UserDataValidator.isValidEmail(email) &&
                    UserDataValidator.isValidPassword(password) &&
                    passwordsMatch,
            onSignUp = {},
            onNavigateToSignInScreen = {},
            requestState = null,
            onCancelRequest = {},
            onErrorClose = {}
        )
    }
}
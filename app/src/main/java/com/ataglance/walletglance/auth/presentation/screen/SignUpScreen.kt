package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapperNew.toResultStateButton
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpRequestViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainer
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignUpScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val signUpViewModel = backStack.sharedKoinNavViewModel<SignUpViewModel>(navController) {
        parametersOf(
            backStack.toRoute<AuthScreens.SignUp>().email
        )
    }
    val signUpRequestViewModel = koinViewModel<SignUpRequestViewModel>()

    val nameState by signUpViewModel.nameState.collectAsStateWithLifecycle()
    val emailState by signUpViewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by signUpViewModel.passwordState.collectAsStateWithLifecycle()
    val confirmPasswordState by signUpViewModel.confirmPasswordState.collectAsStateWithLifecycle()
    val signUpIsAllowed by signUpViewModel.signUpIsAllowed.collectAsStateWithLifecycle()
    val requestState by signUpRequestViewModel.requestState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    SignUpScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        nameState = nameState,
        onNameChange = signUpViewModel::updateAndValidateName,
        emailState = emailState,
        onEmailChange = signUpViewModel::updateAndValidateEmail,
        passwordState = passwordState,
        onPasswordChange = signUpViewModel::updateAndValidatePassword,
        confirmPasswordState = confirmPasswordState,
        onConfirmPasswordChange = signUpViewModel::updateAndValidateConfirmPassword,
        signUpIsAllowed = signUpIsAllowed,
        onSignUp = {
            job = coroutineScope.launch {
                val result = signUpRequestViewModel.signUp(
                    name = nameState.trimmedText,
                    email = emailState.trimmedText,
                    password = passwordState.trimmedText,
                    confirmPassword = confirmPasswordState.trimmedText
                )
                if (!result) return@launch
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
            signUpRequestViewModel.resetRequestState()
        },
        onErrorButton = signUpRequestViewModel::resetRequestState,
    )
}

@Composable
fun SignUpScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
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
    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.User,
        title = stringResource(R.string.create_new_account),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = null,
        onErrorButton = onErrorButton,
        screenTopContent = {
            GlassSurfaceTopNavButtonBlock(
                text = stringResource(R.string.sign_up),
                imageRes = null,
                onClick = onNavigateBack
            )
        },
        screenCenterContent = {
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
                }
                PrimaryButton(
                    text = stringResource(R.string.sign_up),
                    enabled = signUpIsAllowed,
                    onClick = onSignUp
                )
            }
        },
        screenBottomContent = {
            SecondaryButton(
                text = stringResource(R.string.sign_in),
                onClick = onNavigateToSignInScreen
            )
        }
    )
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
            labelText = stringResource(R.string.name),
            placeholderText = stringResource(R.string.name),
            imeAction = ImeAction.Next
        )
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
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = confirmPasswordState,
            onValueChange = onConfirmPasswordChange,
            labelText = stringResource(R.string.confirm_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
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
    val signUpIsAllowed = UserDataValidator.isValidName(name) &&
            UserDataValidator.isValidEmail(email) &&
            UserDataValidator.isValidPassword(password)

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignUpScreen(
            onNavigateBack = {},
            nameState = nameState,
            onNameChange = {},
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            confirmPasswordState = confirmPasswordState,
            onConfirmPasswordChange = {},
            signUpIsAllowed = signUpIsAllowed,
            onSignUp = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.creating_your_identity_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.SignedIn.toResultStateButton()
                    )
                }
            },
            onNavigateToSignInScreen = {},
            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}
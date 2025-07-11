package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.EmailUpdateViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EmailUpdateRequestScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedKoinNavViewModel<EmailUpdateViewModel>(navController)

    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newEmailState by viewModel.newEmailState.collectAsStateWithLifecycle()
    val emailUpdateIsAllowed by viewModel.emailUpdateIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.updateRequestRequestState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    EmailUpdateRequestScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        newEmailState = newEmailState,
        onNewEmailChange = viewModel::updateAndValidateNewEmail,
        emailUpdateIsAllowed = emailUpdateIsAllowed,
        onRequestEmailUpdate = {
            job = coroutineScope.launch {
                if (!viewModel.requestEmailUpdate()) return@launch
                navViewModel.navigateToScreen(
                    navController = navController, screen = AuthScreens.EmailUpdateEmailVerification
                )
            }
        },
        requestState = requestState,
        onCancelRequest = {
            job?.cancel()
            job = null
            viewModel.resetUpdateRequestRequestState()
        },
        onErrorButton = viewModel::resetUpdateRequestRequestState
    )
}

@Composable
fun EmailUpdateRequestScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldState,
    onNewEmailChange: (String) -> Unit,
    emailUpdateIsAllowed: Boolean,
    onRequestEmailUpdate: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Email,
        title = stringResource(R.string.update_your_email),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.update_email),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = {
            GlassSurface(
                filledWidths = FilledWidthByScreenType(compact = .86f)
            ) {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    newEmailState = newEmailState,
                    onNewEmailChange = onNewEmailChange,
                    onRequestUpdateEmail = onRequestEmailUpdate
                )
            }
        },
        screenBottomContent = {
            PrimaryButton(
                text = stringResource(R.string.update_email),
                enabled = emailUpdateIsAllowed,
                onClick = onRequestEmailUpdate
            )
        }
    )



    /*val backButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.email_light_default,
        darkDefault = R.drawable.email_dark_default,
    ).get(CurrAppTheme)

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = onCancelRequest,
        onErrorClose = onErrorButton
    ) {
        ScreenContainerWithBackNavButtonTitleAndGlassSurface(
            onNavigateBack = onNavigateBack,
            backButtonText = stringResource(R.string.update_email),
            backButtonImageRes = backButtonImageRes,
            title = stringResource(R.string.update_your_email),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    newEmailState = newEmailState,
                    onNewEmailChange = onNewEmailChange,
                    onRequestUpdateEmail = onRequestEmailUpdate
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.update_email),
                    enabled = emailUpdateIsAllowed,
                    onClick = onRequestEmailUpdate
                )
            }
        )
    }*/
}

@Composable
private fun GlassSurfaceContent(
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldState,
    onNewEmailChange: (String) -> Unit,
    onRequestUpdateEmail: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            labelText = stringResource(R.string.current_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newEmailState,
            onValueChange = onNewEmailChange,
            labelText = stringResource(R.string.new_email),
            placeholderText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onRequestUpdateEmail
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EmailUpdateRequestScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val password = "_Password1"
    val passwordState = ValidatedFieldState(
        fieldText = password,
        validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password).toUiStates()
    )
    val newEmail = "newEmail@domain.com"
    val newEmailState = ValidatedFieldState(
        fieldText = newEmail,
        validationStates = UserDataValidator.validateEmail(newEmail).toUiStates()
    )
    val emailUpdateIsAllowed = UserDataValidator.isValidPassword(password) &&
            UserDataValidator.isValidEmail(newEmail)

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.requesting_email_update_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        EmailUpdateRequestScreen(
            onNavigateBack = {},
            passwordState = passwordState,
            onPasswordChange = {},
            newEmailState = newEmailState,
            onNewEmailChange = {},
            emailUpdateIsAllowed = emailUpdateIsAllowed,
            onRequestEmailUpdate = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.requesting_email_update_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.EmailUpdateEmailVerificationSent.toResultStateButton()
                    )
                }
            },

            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}
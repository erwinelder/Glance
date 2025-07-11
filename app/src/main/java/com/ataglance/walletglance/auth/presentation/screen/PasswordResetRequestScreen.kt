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
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordResetRequestViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PasswordResetRequestScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<PasswordResetRequestViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.RequestPasswordReset>().email
        )
    }

    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val requestIsAllowed by viewModel.requestIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    PasswordResetRequestScreen(
        screenPadding = screenPadding,
        emailState = emailState,
        onNavigateBack = navController::popBackStack,
        onEmailChange = viewModel::updateAndValidateEmail,
        requestIsAllowed = requestIsAllowed,
        onRequestPasswordReset = viewModel::requestPasswordReset,

        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordResetRequest,
        onSuccessButton = navController::popBackStack,
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun PasswordResetRequestScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    requestIsAllowed: Boolean,
    onRequestPasswordReset: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Password,
        title = stringResource(R.string.request_password_reset),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.reset_password),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = {
            GlassSurface(
                filledWidths = FilledWidthByScreenType(compact = .86f)
            ) {
                GlassSurfaceContent(
                    emailState = emailState,
                    onEmailChange = onEmailChange,
                    onRequestPasswordReset = onRequestPasswordReset
                )
            }
        },
        screenBottomContent = {
            PrimaryButton(
                text = stringResource(R.string.send_email),
                enabled = requestIsAllowed,
                onClick = onRequestPasswordReset
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    onRequestPasswordReset: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = emailState,
            onValueChange = onEmailChange,
            labelText = stringResource(R.string.email),
            placeholderText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onRequestPasswordReset
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun PasswordResetRequestScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val email = "example@domain.com"
    val emailState = ValidatedFieldState(
        fieldText = email,
        validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(email).toUiStates()
    )

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.requesting_password_reset_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        PasswordResetRequestScreen(
            onNavigateBack = {},
            emailState = emailState,
            onEmailChange = {},
            requestIsAllowed = true,
            onRequestPasswordReset = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.requesting_password_reset_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.ResetPasswordEmailSent.toResultStateButton()
                    )
                }
            },

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
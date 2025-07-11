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
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordResetViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
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
fun PasswordResetScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<PasswordResetViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.ResetPassword>().obbCode
        )
    }

    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val confirmNewPasswordState by viewModel.confirmNewPasswordState.collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordResetIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    PasswordResetScreen(
        screenPadding = screenPadding,
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = confirmNewPasswordState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateConfirmNewPassword,
        passwordResetIsAllowed = passwordUpdateIsAllowed,
        onResetPassword = viewModel::resetPassword,

        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordReset,
        onSuccessButton = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = if (appConfiguration.isSetUp) {
                    AuthScreens.Profile
                } else {
                    AuthScreens.SignIn()
                }
            )
        },
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun PasswordResetScreen(
    screenPadding: PaddingValues = PaddingValues(),
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordResetIsAllowed: Boolean,
    onResetPassword: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Password,
        title = stringResource(R.string.reset_your_password),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        screenCenterContent = {
            GlassSurface(
                filledWidths = FilledWidthByScreenType(compact = .86f)
            ) {
                GlassSurfaceContent(
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange,
                    onResetPassword = onResetPassword
                )
            }
        },
        screenBottomContent = {
            PrimaryButton(
                text = stringResource(R.string.reset),
                enabled = passwordResetIsAllowed,
                onClick = onResetPassword
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    onResetPassword: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = newPasswordState,
            onValueChange = onNewPasswordChange,
            labelText = stringResource(R.string.new_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newPasswordConfirmationState,
            onValueChange = onNewPasswordConfirmationChange,
            labelText = stringResource(R.string.new_password_confirmation),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onResetPassword
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun PasswordResetScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newPassword = "_Password1"
    val newPasswordState = ValidatedFieldState(
        fieldText = newPassword,
        validationStates = UserDataValidator.validatePassword(newPassword).toUiStates()
    )
    val confirmNewPassword = "_Password11"
    val confirmNewPasswordState = ValidatedFieldState(
        fieldText = confirmNewPassword,
        validationStates = UserDataValidator
            .validateConfirmationPassword(newPassword, confirmNewPassword)
            .toUiStates()
    )

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.updating_your_password_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        PasswordResetScreen(
            newPasswordState = newPasswordState,
            onNewPasswordChange = {},
            newPasswordConfirmationState = confirmNewPasswordState,
            onNewPasswordConfirmationChange = {},
            passwordResetIsAllowed = true,
            onResetPassword = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.updating_your_password_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.PasswordUpdated.toResultStateButton()
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
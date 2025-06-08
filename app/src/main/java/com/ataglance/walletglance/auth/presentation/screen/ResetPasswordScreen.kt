package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.ataglance.walletglance.auth.presentation.viewmodel.ResetPasswordViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResetPasswordScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<ResetPasswordViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.ResetPassword>().obbCode
        )
    }

    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val confirmNewPasswordState by viewModel.confirmNewPasswordState.collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordResetIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    ResetPasswordScreen(
        screenPadding = screenPadding,
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = confirmNewPasswordState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateConfirmNewPassword,
        passwordResetIsAllowed = passwordUpdateIsAllowed,
        onResetPassword = viewModel::resetPassword,
        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordReset,
        onSuccessClose = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = if (appConfiguration.isSetUp) {
                    AuthScreens.Profile
                } else {
                    AuthScreens.SignIn()
                }
            )
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun ResetPasswordScreen(
    screenPadding: PaddingValues = PaddingValues(),
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordResetIsAllowed: Boolean,
    onResetPassword: () -> Unit,
    requestState: RequestState<ButtonState>?,
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
            title = stringResource(R.string.reset_your_password),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange,
                    onResetPassword = onResetPassword
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.reset),
                    enabled = passwordResetIsAllowed,
                    onClick = onResetPassword
                )
            }
        )
    }
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
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.new_password),
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newPasswordConfirmationState,
            onValueChange = onNewPasswordConfirmationChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.new_password_confirmation),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onResetPassword
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ResetPasswordScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        ResetPasswordScreen(
            newPasswordState = ValidatedFieldState(
                fieldText = newPassword,
                validationStates = UserDataValidator.validatePassword(newPassword).toUiStates()
            ),
            onNewPasswordChange = {},
            newPasswordConfirmationState = ValidatedFieldState(
                fieldText = newPasswordConfirmation,
                validationStates = UserDataValidator
                    .validateConfirmationPassword(newPassword, newPasswordConfirmation)
                    .toUiStates()
            ),
            onNewPasswordConfirmationChange = {},
            passwordResetIsAllowed = true,
            onResetPassword = {},
            requestState = null,
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
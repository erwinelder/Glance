package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.UpdatePasswordViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithBackNavButtonTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpdatePasswordScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<UpdatePasswordViewModel>()

    val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val confirmNewPasswordState by viewModel.confirmNewPasswordState.collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    UpdatePasswordScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        currentPasswordState = currentPasswordState,
        onCurrentPasswordChange = viewModel::updateAndValidatePassword,
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = confirmNewPasswordState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateConfirmNewPassword,
        passwordUpdateIsAllowed = passwordUpdateIsAllowed,
        onUpdatePassword = viewModel::updatePassword,
        onNavigateToRequestPasswordResetScreen = {
            navViewModel.popBackStackAndNavigate(
                navController = navController,
                screen = AuthScreens.RequestPasswordReset()
            )
        },
        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordUpdate,
        onSuccessClose = navController::popBackStack,
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun UpdatePasswordScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    currentPasswordState: ValidatedFieldState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePassword: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    val backButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.password_light_default,
        darkDefault = R.drawable.password_dark_default,
    ).getByTheme(CurrAppTheme)

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
        ScreenContainerWithBackNavButtonTitleAndGlassSurface(
            onNavigateBack = onNavigateBack,
            backButtonText = stringResource(R.string.update_password),
            backButtonImageRes = backButtonImageRes,
            title = stringResource(R.string.update_your_password),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    currentPasswordState = currentPasswordState,
                    onCurrentPasswordChange = onCurrentPasswordChange,
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange,
                    onUpdatePassword = onUpdatePassword
                )
            },
            buttonBlockUnderGlassSurface = {
                PrimaryButton(
                    text = stringResource(R.string.update_password),
                    enabled = passwordUpdateIsAllowed,
                    onClick = onUpdatePassword
                )
            },
            bottomButtonBlock = {
                SecondaryButton(
                    text = stringResource(R.string.reset_password),
                    onClick = onNavigateToRequestPasswordResetScreen
                )
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    currentPasswordState: ValidatedFieldState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    onUpdatePassword: () -> Unit
) {
    val scrollState = rememberScrollState()

    GlassSurfaceContentColumnWrapper(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        SmallTextFieldWithLabelAndMessages(
            state = currentPasswordState,
            onValueChange = onCurrentPasswordChange,
            labelText = stringResource(R.string.current_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
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
            onGoKeyboardAction = onUpdatePassword
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun UpdatePasswordScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newPassword = "_Password1"
    val confirmNewPassword = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        UpdatePasswordScreen(
            onNavigateBack = {},
            currentPasswordState = ValidatedFieldState(),
            onCurrentPasswordChange = {},
            newPasswordState = ValidatedFieldState(
                fieldText = newPassword,
                validationStates = UserDataValidator.validatePassword(newPassword)
                    .toUiStates()
            ),
            onNewPasswordChange = {},
            newPasswordConfirmationState = ValidatedFieldState(
                fieldText = confirmNewPassword,
                validationStates = UserDataValidator
                    .validateConfirmationPassword(newPassword, confirmNewPassword)
                    .toUiStates()
            ),
            onNewPasswordConfirmationChange = {},
            passwordUpdateIsAllowed = true,
            onUpdatePassword = {},
            onNavigateToRequestPasswordResetScreen = {},
            requestState = null,
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
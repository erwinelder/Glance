package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.mapper.toResultState
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreenWrapper(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    backStack: NavBackStackEntry
) {
    val obbCode = backStack.toRoute<AuthScreens.ResetPassword>().obbCode

    val viewModel = backStack.sharedViewModel<AuthViewModel>(
        navController = navController,
        factory = AuthViewModelFactory(email = authController.getEmail())
    )
    LaunchedEffect(true) {
        viewModel.resetAllFieldsExceptEmail()
    }
    LaunchedEffect(obbCode) {
        viewModel.setObbCode(obbCode)
    }

    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
        .collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordResetIsAllowed
        .collectAsStateWithLifecycle()
    val resultState by viewModel.resultState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    ResetPasswordScreen(
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = newPasswordConfirmationState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateNewPasswordConfirmation,
        passwordUpdateIsAllowed = passwordUpdateIsAllowed,
        onUpdatePassword = {
            coroutineScope.launch {
                if (!passwordUpdateIsAllowed) return@launch
                val result = authController.setNewPassword(
                    obbCode = obbCode, newPassword = newPasswordState.fieldText
                )
                when (result) {
                    is Result.Success -> {
                        navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                            navController = navController,
                            screenType = AuthResultSuccessScreenType.PasswordUpdate
                        )
                    }
                    is Result.Error -> viewModel.setResultState(result.toResultState())
                }
            }
        },
        resultState = resultState,
        onResultReset = viewModel::resetResultState
    )
}

@Composable
fun ResetPasswordScreen(
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePassword: () -> Unit,
    resultState: ResultState?,
    onResultReset: () -> Unit
) {
    Box {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.reset_your_password),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange,
                    onUpdatePassword = onUpdatePassword
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.save),
                    enabled = passwordUpdateIsAllowed,
                    onClick = onUpdatePassword
                )
            }
        )
        ResultBottomSheet(
            resultState = resultState,
            onDismissRequest = onResultReset
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    onUpdatePassword: () -> Unit
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
            onGoKeyboardAction = onUpdatePassword
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
            newPasswordState = ValidatedFieldUiState(
                fieldText = newPassword,
                validationStates = UserDataValidator.validatePassword(newPassword).toUiStates()
            ),
            onNewPasswordChange = {},
            newPasswordConfirmationState = ValidatedFieldUiState(
                fieldText = newPasswordConfirmation,
                validationStates = UserDataValidator
                    .validateConfirmationPassword(newPassword, newPasswordConfirmation)
                    .toUiStates()
            ),
            onNewPasswordConfirmationChange = {},
            passwordUpdateIsAllowed = true,
            onUpdatePassword = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
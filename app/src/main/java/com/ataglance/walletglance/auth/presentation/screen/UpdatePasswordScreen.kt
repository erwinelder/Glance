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
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
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
fun UpdatePasswordScreenWrapper(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    authController: AuthController,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedViewModel<AuthViewModel>(
        navController = navController,
        factory = AuthViewModelFactory(email = authController.getEmail())
    )
    LaunchedEffect(true) {
        viewModel.resetAllFieldsExceptEmail()
    }

    val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val newPasswordConfirmationState by viewModel.newPasswordConfirmationState
        .collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed
        .collectAsStateWithLifecycle()
    val resultState by viewModel.resultState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    UpdatePasswordScreen(
        currentPasswordState = currentPasswordState,
        onCurrentPasswordChange = viewModel::updateAndValidatePassword,
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = newPasswordConfirmationState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateNewPasswordConfirmation,
        passwordUpdateIsAllowed = passwordUpdateIsAllowed,
        onUpdatePassword = {
            coroutineScope.launch {
                if (!passwordUpdateIsAllowed) return@launch
                val result = authController.updatePassword(
                    currentPassword = currentPasswordState.fieldText,
                    newPassword = newPasswordState.fieldText
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
        onNavigateToRequestPasswordResetScreen = {
            navViewModel.popBackStackAndNavigate(
                navController = navController,
                screen = AuthScreens.RequestPasswordReset
            )
        },
        resultState = resultState,
        onResultReset = viewModel::resetResultState
    )
}

@Composable
fun UpdatePasswordScreen(
    currentPasswordState: ValidatedFieldUiState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePassword: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    resultState: ResultState?,
    onResultReset: () -> Unit
) {
    Box {
        ScreenContainerWithTitleAndGlassSurface(
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
        ResultBottomSheet(
            resultState = resultState,
            onDismissRequest = onResultReset
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    currentPasswordState: ValidatedFieldUiState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    onUpdatePassword: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = currentPasswordState,
            onValueChange = onCurrentPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.current_password),
            imeAction = ImeAction.Next
        )
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
fun UpdatePasswordScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        UpdatePasswordScreen(
            currentPasswordState = ValidatedFieldUiState(),
            onCurrentPasswordChange = {},
            newPasswordState = ValidatedFieldUiState(
                fieldText = newPassword,
                validationStates = UserDataValidator.validatePassword(newPassword)
                    .toUiStates()
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
            onNavigateToRequestPasswordResetScreen = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
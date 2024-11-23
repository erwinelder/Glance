package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg

@Composable
fun UpdatePasswordScreen(
    currentPasswordState: ValidatedFieldUiState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePasswordButtonClick: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit
) {
    Box {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.update_your_password),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    currentPasswordState = currentPasswordState,
                    onCurrentPasswordChange = onCurrentPasswordChange,
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange
                )
            },
            buttonUnderGlassSurface = {
                PrimaryButton(
                    text = stringResource(R.string.update_password),
                    enabled = passwordUpdateIsAllowed,
                    onClick = onUpdatePasswordButtonClick
                )
            },
            bottomButton = {
                SecondaryButton(
                    text = stringResource(R.string.reset_password),
                    onClick = onNavigateToRequestPasswordResetScreen
                )
            }
        )
        ResultBottomSheet(
            resultState = resultState,
            onSheetClose = onResultReset
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
    onNewPasswordConfirmationChange: (String) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithLabelAndErrorMsg(
            state = currentPasswordState,
            onValueChange = onCurrentPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.current_password)
        )
        TextFieldWithLabelAndErrorMsg(
            state = newPasswordState,
            onValueChange = onNewPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.new_password)
        )
        TextFieldWithLabelAndErrorMsg(
            state = newPasswordConfirmationState,
            onValueChange = onNewPasswordConfirmationChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.new_password_confirmation)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun UpdatePasswordScreenPreview() {
    val userDataValidator = UserDataValidator()

    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        UpdatePasswordScreen(
            currentPasswordState = ValidatedFieldUiState(),
            onCurrentPasswordChange = {},
            newPasswordState = ValidatedFieldUiState(
                fieldText = newPassword,
                validationStates = userDataValidator.validatePassword(newPassword)
                    .toUiStates()
            ),
            onNewPasswordChange = {},
            newPasswordConfirmationState = ValidatedFieldUiState(
                fieldText = newPasswordConfirmation,
                validationStates = userDataValidator
                    .validateConfirmationPassword(newPassword, newPasswordConfirmation)
                    .toUiStates()
            ),
            onNewPasswordConfirmationChange = {},
            passwordUpdateIsAllowed = true,
            onUpdatePasswordButtonClick = {},
            onNavigateToRequestPasswordResetScreen = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
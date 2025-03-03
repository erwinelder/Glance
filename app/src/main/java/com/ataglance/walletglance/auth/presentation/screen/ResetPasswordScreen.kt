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
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState

@Composable
fun ResetPasswordScreen(
    newPasswordState: ValidatedFieldUiState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldUiState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePasswordButtonClick: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit,
) {
    Box {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.reset_your_password),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    newPasswordState = newPasswordState,
                    onNewPasswordChange = onNewPasswordChange,
                    newPasswordConfirmationState = newPasswordConfirmationState,
                    onNewPasswordConfirmationChange = onNewPasswordConfirmationChange
                )
            },
            bottomButton = {
                PrimaryButton(
                    text = stringResource(R.string.save),
                    enabled = passwordUpdateIsAllowed,
                    onClick = onUpdatePasswordButtonClick
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
    onNewPasswordConfirmationChange: (String) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
fun ResetPasswordScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val userDataValidator = UserDataValidator()

    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        ResetPasswordScreen(
            newPasswordState = ValidatedFieldUiState(
                fieldText = newPassword,
                validationStates = userDataValidator.validatePassword(newPassword).toUiStates()
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
            resultState = null,
            onResultReset = {}
        )
    }
}
package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.errorHandling.domain.model.FieldWithValidationState
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.utils.validateConfirmationPassword
import com.ataglance.walletglance.core.utils.validatePassword

@Composable
fun UpdatePasswordScreen(
    currentPasswordState: FieldWithValidationState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: FieldWithValidationState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: FieldWithValidationState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePasswordButtonClick: () -> Unit
) {
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
        bottomButton = {
            PrimaryButton(
                text = stringResource(R.string.update_password),
                enabled = passwordUpdateIsAllowed,
                onClick = onUpdatePasswordButtonClick
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    currentPasswordState: FieldWithValidationState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: FieldWithValidationState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: FieldWithValidationState,
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
    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        UpdatePasswordScreen(
            currentPasswordState = FieldWithValidationState(),
            onCurrentPasswordChange = {},
            newPasswordState = FieldWithValidationState(
                fieldText = newPassword,
                validationStates = newPassword.validatePassword()
            ),
            onNewPasswordChange = {},
            newPasswordConfirmationState = FieldWithValidationState(
                fieldText = newPasswordConfirmation,
                validationStates = newPasswordConfirmation
                    .validateConfirmationPassword(newPassword)
            ),
            onNewPasswordConfirmationChange = {},
            passwordUpdateIsAllowed = true,
            onUpdatePasswordButtonClick = {}
        )
    }
}
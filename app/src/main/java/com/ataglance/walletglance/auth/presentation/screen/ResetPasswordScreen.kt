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
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.validateConfirmationPassword
import com.ataglance.walletglance.core.utils.validatePassword
import com.ataglance.walletglance.errorHandling.domain.model.FieldWithValidationState
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg

@Composable
fun ResetPasswordScreen(
    newPasswordState: FieldWithValidationState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: FieldWithValidationState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePasswordButtonClick: () -> Unit
) {
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
}

@Composable
private fun GlassSurfaceContent(
    newPasswordState: FieldWithValidationState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: FieldWithValidationState,
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
fun ResetPasswordScreenPreview() {
    val newPassword = "_Password1"
    val newPasswordConfirmation = "_Password11"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        ResetPasswordScreen(
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
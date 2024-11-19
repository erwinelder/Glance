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
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.validateEmail
import com.ataglance.walletglance.errorHandling.domain.model.FieldWithValidationState
import com.ataglance.walletglance.errorHandling.domain.model.TaskResult
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ErrorMessageBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg

@Composable
fun RequestPasswordResetScreen(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    requestIsAllowed: Boolean,
    onRequestPasswordResetButtonClick: () -> Unit,
    taskResult: TaskResult?,
    onTaskResultReset: () -> Unit
) {
    Box {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.request_password_reset),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    emailState = emailState,
                    onEmailChange = onEmailChange
                )
            },
            bottomButton = {
                PrimaryButton(
                    text = stringResource(R.string.send_email),
                    enabled = requestIsAllowed,
                    onClick = onRequestPasswordResetButtonClick
                )
            }
        )
        ErrorMessageBottomSheet(
            taskResult = taskResult,
            onTaskResultReset = onTaskResultReset
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithLabelAndErrorMsg(
            state = emailState,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun RequestPasswordResetScreenPreview() {
    val email = "example@gmail.com"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        RequestPasswordResetScreen(
            emailState = FieldWithValidationState(
                fieldText = email,
                validationStates = email.validateEmail()
            ),
            onEmailChange = {},
            requestIsAllowed = true,
            onRequestPasswordResetButtonClick = {},
            taskResult = null,
            onTaskResultReset = {}
        )
    }
}
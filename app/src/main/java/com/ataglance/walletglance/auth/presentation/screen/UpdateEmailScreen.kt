package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun UpdateEmailScreen(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldUiState,
    onNewEmailChange: (String) -> Unit,
    emailUpdateIsAllowed: Boolean,
    onUpdateEmailButtonClick: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.update_your_email),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    newEmailState = newEmailState,
                    onNewEmailChange = onNewEmailChange
                )
            },
            bottomButton = {
                PrimaryButton(
                    text = stringResource(R.string.update_email),
                    enabled = emailUpdateIsAllowed,
                    onClick = onUpdateEmailButtonClick
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
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldUiState,
    onNewEmailChange: (String) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithLabelAndErrorMsg(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.current_password)
        )
        TextFieldWithLabelAndErrorMsg(
            state = newEmailState,
            onValueChange = onNewEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.new_email)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun UpdateEmailScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val userDataValidator = UserDataValidator()

    val newEmail = "newEmail@domain.com"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        UpdateEmailScreen(
            passwordState = ValidatedFieldUiState(),
            onPasswordChange = {},
            newEmailState = ValidatedFieldUiState(
                fieldText = newEmail,
                validationStates = userDataValidator.validateEmail(newEmail).toUiStates()
            ),
            onNewEmailChange = {},
            emailUpdateIsAllowed = true,
            onUpdateEmailButtonClick = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
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
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
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
fun DeleteAccountScreen(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    deletionIsAllowed: Boolean,
    onDeleteAccount: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit
) {
    Box {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.delete_your_account_with_all_data),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                )
            },
            bottomButton = {
                PrimaryButton(
                    text = stringResource(R.string.delete_account),
                    enabled = deletionIsAllowed,
                    enabledGradient = GlanceColors.errorGradientPair,
                    onClick = onDeleteAccount
                )
            }
        )
        ResultBottomSheet(resultState = resultState, onDismissRequest = onResultReset)
    }
}

@Composable
private fun GlassSurfaceContent(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithLabelAndErrorMsg(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun DeleteAccountScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val userDataValidator = UserDataValidator()

    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        DeleteAccountScreen(
            passwordState = ValidatedFieldUiState(
                fieldText = password,
                validationStates = userDataValidator.validatePassword(password).toUiStates()
            ),
            onPasswordChange = {},
            deletionIsAllowed = userDataValidator.isValidPassword(password),
            onDeleteAccount = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState

@Composable
fun SignUpScreen(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: ValidatedFieldUiState,
    onConfirmPasswordChange: (String) -> Unit,
    signUpIsAllowed: Boolean,
    onCreateNewUserWithEmailAndPassword: (String, String) -> Unit,
    onNavigateToSignInScreen: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit
) {
    Box {
        GlassSurfaceScreenContainerWithTitle(
            title = stringResource(R.string.create_new_account),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    emailState = emailState,
                    onEmailChange = onEmailChange,
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    confirmPasswordState = confirmPasswordState,
                    onConfirmPasswordChange = onConfirmPasswordChange
                )
            },
            buttonUnderGlassSurface = {
                PrimaryButton(
                    text = stringResource(R.string.sign_up),
                    enabled = signUpIsAllowed
                ) {
                    onCreateNewUserWithEmailAndPassword(emailState.fieldText, passwordState.fieldText)
                }
            },
            bottomButton = {
                SecondaryButton(
                    text = stringResource(R.string.sign_in),
                    onClick = onNavigateToSignInScreen
                )
            }
        )
        ResultBottomSheet(resultState = resultState, onDismissRequest = onResultReset)
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: ValidatedFieldUiState,
    onConfirmPasswordChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        TextFieldWithLabelAndErrorMsg(
            state = emailState,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email)
        )
        TextFieldWithLabelAndErrorMsg(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password)
        )
        TextFieldWithLabelAndErrorMsg(
            state = confirmPasswordState,
            onValueChange = onConfirmPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.confirm_password)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignUpScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val userDataValidator = UserDataValidator()

    val email = "example@domain.com"
    val password = "_Password1"
    val confirmPassword = "_Password11"
    val passwordsMatch = true

    val emailState = ValidatedFieldUiState(
        fieldText = email,
        validationStates = userDataValidator.validateEmail(email).toUiStates()
    )
    val passwordState = ValidatedFieldUiState(
        fieldText = password,
        validationStates = userDataValidator.validatePassword(password).toUiStates()
    )
    val confirmPasswordState = ValidatedFieldUiState(
        fieldText = confirmPassword,
        validationStates = userDataValidator
            .validateConfirmationPassword(password, confirmPassword)
            .toUiStates()
    )

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignUpScreen(
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            confirmPasswordState = confirmPasswordState,
            onConfirmPasswordChange = {},
            signUpIsAllowed = userDataValidator.isValidEmail(email) &&
                    userDataValidator.isValidPassword(password) &&
                    passwordsMatch,
            onCreateNewUserWithEmailAndPassword = { _, _ -> },
            onNavigateToSignInScreen = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
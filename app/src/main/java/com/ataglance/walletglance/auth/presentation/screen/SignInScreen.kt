package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState

@Composable
fun SignInScreen(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignIn: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,
    resultState: ResultUiState?,
    onResultReset: () -> Unit,
    onNavigateToSignUpScreen: (() -> Unit)?
) {
    Box {

        if (onNavigateToSignUpScreen != null) {
            GlassSurfaceScreenContainerWithTitle(
                title = stringResource(R.string.sign_in_to_your_account),
                glassSurfaceContent = {
                    GlassSurfaceContent(
                        emailState = emailState,
                        onEmailChange = onEmailChange,
                        passwordState = passwordState,
                        onPasswordChange = onPasswordChange,
                    )
                },
                buttonUnderGlassSurface = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PrimaryButton(
                            text = stringResource(R.string.sign_in),
                            enabled = signInIsAllowed,
                            onClick = onSignIn
                        )
                        SecondaryButton(
                            text = stringResource(R.string.reset_password),
                            onClick = onNavigateToRequestPasswordResetScreen
                        )
                        // TODO add continue as guest button
                    }
                },
                bottomButton = {
                    SecondaryButton(
                        text = stringResource(R.string.sign_up),
                        onClick = onNavigateToSignUpScreen
                    )
                }
            )
        } else {
            GlassSurfaceScreenContainerWithTitle(
                title = stringResource(R.string.sign_in_to_your_account),
                glassSurfaceContent = {
                    GlassSurfaceContent(
                        emailState = emailState,
                        onEmailChange = onEmailChange,
                        passwordState = passwordState,
                        onPasswordChange = onPasswordChange,
                    )
                },
                buttonUnderGlassSurface = {
                    PrimaryButton(
                        text = stringResource(R.string.sign_in),
                        enabled = signInIsAllowed,
                        onClick = onSignIn
                    )
                },
                bottomButton = {
                    SecondaryButton(
                        text = stringResource(R.string.reset_password),
                        onClick = onNavigateToRequestPasswordResetScreen
                    )
                }
            )
        }

        ResultBottomSheet(resultState = resultState, onDismissRequest = onResultReset)
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
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
fun SignInScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val userDataValidator = UserDataValidator()

    val email = "example@domain.com"
    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignInScreen(
            emailState = ValidatedFieldUiState(
                fieldText = email,
                validationStates = userDataValidator.validateEmail(email).toUiStates()
            ),
            onEmailChange = {},
            passwordState = ValidatedFieldUiState(
                fieldText = password,
                validationStates = userDataValidator.validateRequiredFieldIsNotEmpty(password)
                    .toUiStates()
            ),
            onPasswordChange = {},
            signInIsAllowed = userDataValidator.isValidEmail(email) &&
                    userDataValidator.isValidPassword(password),
            onSignIn = {},
            onNavigateToRequestPasswordResetScreen = {},
            resultState = null,
            onResultReset = {},
            onNavigateToSignUpScreen = {}
        )
    }
}

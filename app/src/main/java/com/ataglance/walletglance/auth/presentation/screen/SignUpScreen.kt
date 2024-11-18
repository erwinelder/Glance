package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
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
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.errorHandling.domain.model.FieldWithValidationState
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.fields.TextFieldWithLabelAndErrorMsg
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.utils.isValidEmail
import com.ataglance.walletglance.core.utils.isValidPassword
import com.ataglance.walletglance.core.utils.validateConfirmationPassword
import com.ataglance.walletglance.core.utils.validateEmail
import com.ataglance.walletglance.core.utils.validatePassword

@Composable
fun SignUpScreen(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    passwordState: FieldWithValidationState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: FieldWithValidationState,
    onConfirmPasswordChange: (String) -> Unit,
    signUpIsAllowed: Boolean,
    onCreateNewUserWithEmailAndPassword: (String, String) -> Unit,
    onNavigateToSignInScreen: () -> Unit
) {

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
}

@Composable
private fun GlassSurfaceContent(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    passwordState: FieldWithValidationState,
    onPasswordChange: (String) -> Unit,
    confirmPasswordState: FieldWithValidationState,
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
fun SignUpScreenPreview() {
    val email = "example@domain.com"
    val password = "_Password1"
    val confirmPassword = "_Password1"
    val passwordsMatch = true

    val emailState = FieldWithValidationState(
        fieldText = email,
        validationStates = email.validateEmail()
    )
    val passwordState = FieldWithValidationState(
        fieldText = password,
        validationStates = password.validatePassword()
    )
    val confirmPasswordState = FieldWithValidationState(
        fieldText = confirmPassword,
        validationStates = password.validateConfirmationPassword(confirmPassword)
    )

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        SignUpScreen(
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            confirmPasswordState = confirmPasswordState,
            onConfirmPasswordChange = {},
            signUpIsAllowed = email.isValidEmail() && password.isValidPassword() && passwordsMatch,
            onCreateNewUserWithEmailAndPassword = { _, _ -> },
            onNavigateToSignInScreen = {}
        )
    }
}
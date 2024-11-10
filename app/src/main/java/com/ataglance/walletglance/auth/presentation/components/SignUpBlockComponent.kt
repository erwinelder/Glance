package com.ataglance.walletglance.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.componentState.FieldValidationState
import com.ataglance.walletglance.core.domain.componentState.FieldWithValidationState
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabelAndErrorMsg

@Composable
fun SignUpBlockComponent(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    passwordState: FieldWithValidationState,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onCreateNewUserWithEmailAndPassword: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlassSurface {
            GlassSurfaceContent(
                emailState = emailState,
                onEmailChange = onEmailChange,
                passwordState = passwordState,
                onPasswordChange = onPasswordChange,
            )
        }
        PrimaryButton(
            text = stringResource(R.string.sign_up),
            enabled = signInIsAllowed
        ) {
            onCreateNewUserWithEmailAndPassword(emailState.fieldText, passwordState.fieldText)
        }
    }

}

@Composable
private fun GlassSurfaceContent(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    passwordState: FieldWithValidationState,
    onPasswordChange: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
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
private fun SignUpBlockComponentPreview() {
    val emailState = FieldWithValidationState(
        fieldText = "",
        validationStates = listOf(
            FieldValidationState(isValid = true, messageRes = R.string.required_field)
        )
    )
    val passwordState = FieldWithValidationState(
        fieldText = "",
        validationStates = listOf(
            FieldValidationState(isValid = false, messageRes = R.string.required_field)
        )
    )

    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        SignUpBlockComponent(
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            signInIsAllowed = true,
            onCreateNewUserWithEmailAndPassword = { _, _ -> }
        )
    }
}
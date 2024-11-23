package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
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
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainerWithTitle
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer

@Composable
fun SignInScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignInWithEmailAndPassword: (String, String) -> Unit,
    onNavigateToSignUpScreen: () -> Unit
) {
    GlassSurfaceScreenContainerWithTitle(
        title = stringResource(R.string.sign_in_to_your_account),
        glassSurfaceContent = {
            GlassSurfaceContent(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
            )
        },
        buttonUnderGlassSurface = {
            PrimaryButton(
                text = stringResource(R.string.sign_in),
                enabled = signInIsAllowed
            ) {
                onSignInWithEmailAndPassword(email, password)
            }
        },
        bottomButton = {
            SecondaryButton(
                text = stringResource(R.string.sign_up),
                onClick = onNavigateToSignUpScreen
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    GlassSurfaceContentColumnWrapper(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextFieldWithLabel(
            text = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email)
        )
        TextFieldWithLabel(
            text = password,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password)
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignInScreenPreview() {
    val userDataValidator = UserDataValidator()

    val email = "example@domain.com"
    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        SignInScreen(
            email = email,
            onEmailChange = {},
            password = password,
            onPasswordChange = {},
            signInIsAllowed = userDataValidator.isValidEmail(email) &&
                    userDataValidator.isValidPassword(password),
            onSignInWithEmailAndPassword = { _, _ -> },
            onNavigateToSignUpScreen = {}
        )
    }
}

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
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel

@Composable
fun SignInBlockComponent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    signInIsAllowed: Boolean,
    onSignInWithEmailAndPassword: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlassSurface {
            GlassSurfaceContent(
                email = email,
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
            )
        }
        PrimaryButton(
            text = stringResource(R.string.sign_in),
            enabled = signInIsAllowed
        ) {
            onSignInWithEmailAndPassword(email, password)
        }
    }
}

@Composable
private fun GlassSurfaceContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
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
private fun SignInBlockComponentPreview() {
    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        SignInBlockComponent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            signInIsAllowed = true,
            onSignInWithEmailAndPassword = { _, _ -> }
        )
    }
}
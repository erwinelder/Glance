package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainer

@Composable
fun AuthScreen(
    onSignInWithEmailAndPassword: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    GlassSurfaceContainer(
        glassSurfaceContent = {
            GlassSurfaceContent(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it }
            )
        },
        fillGlassSurface = false,
        primaryBottomButton = {
            PrimaryButton(text = stringResource(R.string.sign_in)) {
                onSignInWithEmailAndPassword(email, password)
            }
        }
    )
}

@Composable
fun GlassSurfaceContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
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
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email)
        )
        TextFieldWithLabel(
            text = password,
            onValueChange = onPasswordChange,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password),
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AuthScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        AuthScreen(
            onSignInWithEmailAndPassword = { _, _ -> }
        )
    }
}

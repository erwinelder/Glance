package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.presentation.components.SignInBlockComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.isValidEmail
import com.ataglance.walletglance.core.utils.isValidPassword

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
    Column(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Welcome back to Glance!",
            style = Typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(
                    FilledWidthByScreenType().getByScreenType(LocalWindowType.current)
                )
        )
        Spacer(modifier = Modifier.height(32.dp))
        SignInBlockComponent(
            email = email,
            onEmailChange = onEmailChange,
            password = password,
            onPasswordChange = onPasswordChange,
            signInIsAllowed = signInIsAllowed,
            onSignInWithEmailAndPassword = onSignInWithEmailAndPassword,
        )
        Spacer(modifier = Modifier.height(32.dp))
        SecondaryButton(
            text = stringResource(R.string.sign_up),
            onClick = onNavigateToSignUpScreen
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignInScreenPreview() {
    val email = "example@domain.com"
    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        SignInScreen(
            email = email,
            onEmailChange = {},
            password = password,
            onPasswordChange = {},
            signInIsAllowed = email.isValidEmail() && password.isValidPassword(),
            onSignInWithEmailAndPassword = { _, _ -> },
            onNavigateToSignUpScreen = {}
        )
    }
}

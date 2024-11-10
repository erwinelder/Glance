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
import com.ataglance.walletglance.auth.presentation.components.SignUpBlockComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.componentState.FieldWithValidationState
import com.ataglance.walletglance.core.presentation.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.isValidEmail
import com.ataglance.walletglance.core.utils.isValidPassword
import com.ataglance.walletglance.core.utils.validateEmail
import com.ataglance.walletglance.core.utils.validatePassword

@Composable
fun SignUpScreen(
    emailState: FieldWithValidationState,
    onEmailChange: (String) -> Unit,
    passwordState: FieldWithValidationState,
    onPasswordChange: (String) -> Unit,
    signUpIsAllowed: Boolean,
    onCreateNewUserWithEmailAndPassword: (String, String) -> Unit,
    onNavigateToSignInScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Welcome to Glance!",
            style = Typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(
                    FilledWidthByScreenType().getByScreenType(LocalWindowType.current)
                )
        )
        Spacer(modifier = Modifier.height(32.dp))
        SignUpBlockComponent(
            emailState = emailState,
            onEmailChange = onEmailChange,
            passwordState = passwordState,
            onPasswordChange = onPasswordChange,
            signInIsAllowed = signUpIsAllowed,
            onCreateNewUserWithEmailAndPassword = onCreateNewUserWithEmailAndPassword,
        )
        Spacer(modifier = Modifier.height(32.dp))
        SecondaryButton(
            text = stringResource(R.string.sign_in),
            onClick = onNavigateToSignInScreen
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SignUpScreenPreview() {
    val email = "example@domain.com"
    val password = "_Password1"

    val emailState = FieldWithValidationState(
        fieldText = email,
        validationStates = email.validateEmail()
    )
    val passwordState = FieldWithValidationState(
        fieldText = password,
        validationStates = password.validatePassword()
    )

    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        SignUpScreen(
            emailState = emailState,
            onEmailChange = {},
            passwordState = passwordState,
            onPasswordChange = {},
            signUpIsAllowed = email.isValidEmail() && password.isValidPassword(),
            onCreateNewUserWithEmailAndPassword = { _, _ -> },
            onNavigateToSignInScreen = {}
        )
    }
}
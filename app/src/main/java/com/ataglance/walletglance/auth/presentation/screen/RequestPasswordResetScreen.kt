package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.errorHandling.mapper.toResultState
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import kotlinx.coroutines.launch

@Composable
fun RequestPasswordResetScreenWrapper(
    navController: NavHostController,
    authController: AuthController,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedViewModel<AuthViewModel>(
        navController = navController,
        factory = AuthViewModelFactory(email = authController.getEmail())
    )
    LaunchedEffect(true) {
        viewModel.resetAllFieldsExceptEmail()
    }

    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val emailIsValid by viewModel.emailIsValid.collectAsStateWithLifecycle()
    val resultState by viewModel.resultState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    RequestPasswordResetScreen(
        emailState = emailState,
        onEmailChange = viewModel::updateAndValidateEmail,
        requestIsAllowed = emailIsValid,
        onRequestPasswordReset = {
            coroutineScope.launch {
                if (!emailIsValid) return@launch
                val result = authController.requestPasswordReset(email = emailState.fieldText)
                viewModel.setResultState(result.toResultState())
            }
        },
        resultState = resultState,
        onResultReset = viewModel::resetResultState
    )
}

@Composable
fun RequestPasswordResetScreen(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    requestIsAllowed: Boolean,
    onRequestPasswordReset: () -> Unit,
    resultState: ResultState?,
    onResultReset: () -> Unit
) {
    Box {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.request_password_reset),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    emailState = emailState,
                    onEmailChange = onEmailChange,
                    onRequestPasswordReset = onRequestPasswordReset
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.send_email),
                    enabled = requestIsAllowed,
                    onClick = onRequestPasswordReset
                )
            }
        )
        ResultBottomSheet(
            resultState = resultState,
            onDismissRequest = onResultReset
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldUiState,
    onEmailChange: (String) -> Unit,
    onRequestPasswordReset: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = emailState,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.email),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onRequestPasswordReset
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun RequestPasswordResetScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val email = "example@gmail.com"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        RequestPasswordResetScreen(
            emailState = ValidatedFieldUiState(
                fieldText = email,
                validationStates = UserDataValidator.validateEmail(email).toUiStates()
            ),
            onEmailChange = {},
            requestIsAllowed = true,
            onRequestPasswordReset = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
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
fun UpdateEmailScreenWrapper(
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

    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newEmailState by viewModel.newEmailState.collectAsStateWithLifecycle()
    val emailUpdateIsAllowed by viewModel.emailUpdateIsAllowed.collectAsStateWithLifecycle()
    val resultState by viewModel.resultState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    UpdateEmailScreen(
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        newEmailState = newEmailState,
        onNewEmailChange = viewModel::updateAndValidateNewEmail,
        emailUpdateIsAllowed = emailUpdateIsAllowed,
        onUpdateEmail = {
            coroutineScope.launch {
                if (!emailUpdateIsAllowed) return@launch
                val result = authController.requestEmailUpdate(
                    password = passwordState.fieldText, newEmail = newEmailState.fieldText
                )
                viewModel.setResultState(result.toResultState())
            }
        },
        resultState = resultState,
        onResultReset = viewModel::resetResultState
    )
}

@Composable
fun UpdateEmailScreen(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldUiState,
    onNewEmailChange: (String) -> Unit,
    emailUpdateIsAllowed: Boolean,
    onUpdateEmail: () -> Unit,
    resultState: ResultState?,
    onResultReset: () -> Unit
) {
    Box {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.update_your_email),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    newEmailState = newEmailState,
                    onNewEmailChange = onNewEmailChange,
                    onUpdateEmail = onUpdateEmail
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.update_email),
                    enabled = emailUpdateIsAllowed,
                    onClick = onUpdateEmail
                )
            }
        )
        ResultBottomSheet(resultState = resultState, onDismissRequest = onResultReset)
    }
}

@Composable
private fun GlassSurfaceContent(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldUiState,
    onNewEmailChange: (String) -> Unit,
    onUpdateEmail: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.current_password),
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newEmailState,
            onValueChange = onNewEmailChange,
            keyboardType = KeyboardType.Email,
            placeholderText = stringResource(R.string.email),
            labelText = stringResource(R.string.new_email),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onUpdateEmail
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun UpdateEmailScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newEmail = "newEmail@domain.com"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        UpdateEmailScreen(
            passwordState = ValidatedFieldUiState(),
            onPasswordChange = {},
            newEmailState = ValidatedFieldUiState(
                fieldText = newEmail,
                validationStates = UserDataValidator.validateEmail(newEmail).toUiStates()
            ),
            onNewEmailChange = {},
            emailUpdateIsAllowed = true,
            onUpdateEmail = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
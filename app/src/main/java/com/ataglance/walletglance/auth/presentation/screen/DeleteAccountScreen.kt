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
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.mapper.toResultState
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.components.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun DeleteAccountScreenWrapper(
    navController: NavHostController,
    navViewModel: NavigationViewModel,
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
    val deletionIsAllowed by viewModel.signInIsAllowed.collectAsStateWithLifecycle()
    val resultState by viewModel.resultState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    DeleteAccountScreen(
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        deletionIsAllowed = deletionIsAllowed,
        onDeleteAccount = {
            coroutineScope.launch {
                if (!deletionIsAllowed) return@launch
                val result = authController.deleteAccount(
                    password = passwordState.fieldText
                )

                when (result) {
                    is Result.Success -> {
                        navViewModel.popBackStackAndNavigateToResultSuccessScreen(
                            navController = navController,
                            screenType = AuthResultSuccessScreenType.AccountDeletion
                        )
                    }
                    is Result.Error -> {
                        viewModel.setResultState(result.toResultState())
                    }
                }
            }
        },
        resultState = resultState,
        onResultReset = viewModel::resetResultState
    )
}

@Composable
fun DeleteAccountScreen(
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    deletionIsAllowed: Boolean,
    onDeleteAccount: () -> Unit,
    resultState: ResultState?,
    onResultReset: () -> Unit
) {
    Box {
        ScreenContainerWithTitleAndGlassSurface(
            title = stringResource(R.string.delete_your_account_with_all_data),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    onDeleteAccount = onDeleteAccount
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.delete_account),
                    enabled = deletionIsAllowed,
                    enabledGradient = GlanceColors.errorGradientPair,
                    onClick = onDeleteAccount
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
    onDeleteAccount: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            keyboardType = KeyboardType.Password,
            placeholderText = stringResource(R.string.password),
            labelText = stringResource(R.string.password),
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onDeleteAccount
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun DeleteAccountScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val password = "_Password1"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        DeleteAccountScreen(
            passwordState = ValidatedFieldUiState(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotEmpty(password)
                    .toUiStates()
            ),
            onPasswordChange = {},
            deletionIsAllowed = UserDataValidator.isValidPassword(password),
            onDeleteAccount = {},
            resultState = null,
            onResultReset = {}
        )
    }
}
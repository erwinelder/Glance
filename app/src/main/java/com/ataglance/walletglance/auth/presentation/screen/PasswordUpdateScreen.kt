package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.presentation.viewmodel.PasswordUpdateViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainer
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PasswordUpdateScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<PasswordUpdateViewModel>()

    val currentPasswordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newPasswordState by viewModel.newPasswordState.collectAsStateWithLifecycle()
    val confirmNewPasswordState by viewModel.confirmNewPasswordState.collectAsStateWithLifecycle()
    val passwordUpdateIsAllowed by viewModel.passwordUpdateIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    PasswordUpdateScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        currentPasswordState = currentPasswordState,
        onCurrentPasswordChange = viewModel::updateAndValidatePassword,
        newPasswordState = newPasswordState,
        onNewPasswordChange = viewModel::updateAndValidateNewPassword,
        newPasswordConfirmationState = confirmNewPasswordState,
        onNewPasswordConfirmationChange = viewModel::updateAndValidateConfirmNewPassword,
        passwordUpdateIsAllowed = passwordUpdateIsAllowed,
        onUpdatePassword = viewModel::updatePassword,
        onNavigateToRequestPasswordResetScreen = {
            navViewModel.popBackStackAndNavigate(
                navController = navController,
                screen = AuthScreens.RequestPasswordReset()
            )
        },

        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordUpdate,
        onSuccessButton = navController::popBackStack,
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun PasswordUpdateScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    currentPasswordState: ValidatedFieldState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    passwordUpdateIsAllowed: Boolean,
    onUpdatePassword: () -> Unit,
    onNavigateToRequestPasswordResetScreen: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Password,
        title = stringResource(R.string.update_your_password),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        screenTopContent = {
            GlassSurfaceTopNavButtonBlock(
                text = stringResource(R.string.update_password),
                imageRes = null,
                onClick = onNavigateBack
            )
        },
        screenCenterContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GlassSurface(
                    filledWidths = FilledWidthByScreenType(compact = .86f),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    GlassSurfaceContent(
                        currentPasswordState = currentPasswordState,
                        onCurrentPasswordChange = onCurrentPasswordChange,
                        newPasswordState = newPasswordState,
                        onNewPasswordChange = onNewPasswordChange,
                        newPasswordConfirmationState = newPasswordConfirmationState,
                        onNewPasswordConfirmationChange = onNewPasswordConfirmationChange,
                        onUpdatePassword = onUpdatePassword
                    )
                }
                PrimaryButton(
                    text = stringResource(R.string.update_password),
                    enabled = passwordUpdateIsAllowed,
                    onClick = onUpdatePassword
                )
            }
        },
        screenBottomContent = {
            SecondaryButton(
                text = stringResource(R.string.reset_password),
                onClick = onNavigateToRequestPasswordResetScreen
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    currentPasswordState: ValidatedFieldState,
    onCurrentPasswordChange: (String) -> Unit,
    newPasswordState: ValidatedFieldState,
    onNewPasswordChange: (String) -> Unit,
    newPasswordConfirmationState: ValidatedFieldState,
    onNewPasswordConfirmationChange: (String) -> Unit,
    onUpdatePassword: () -> Unit
) {
    val scrollState = rememberScrollState()

    GlassSurfaceContentColumnWrapper(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        SmallTextFieldWithLabelAndMessages(
            state = currentPasswordState,
            onValueChange = onCurrentPasswordChange,
            labelText = stringResource(R.string.current_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newPasswordState,
            onValueChange = onNewPasswordChange,
            labelText = stringResource(R.string.new_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newPasswordConfirmationState,
            onValueChange = onNewPasswordConfirmationChange,
            labelText = stringResource(R.string.new_password_confirmation),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onUpdatePassword
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun PasswordUpdateScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val password = "_Password1"
    val passwordState = ValidatedFieldState(
        fieldText = password,
        validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password).toUiStates()
    )

    val newPassword = "_Password11"
    val newPasswordState = ValidatedFieldState(
        fieldText = newPassword,
        validationStates = UserDataValidator.validatePassword(newPassword).toUiStates()
    )

    val confirmNewPassword = "_Password11"
    val confirmNewPasswordState = ValidatedFieldState(
        fieldText = newPassword,
        validationStates = UserDataValidator
            .validateConfirmationPassword(newPassword, confirmNewPassword).toUiStates()
    )
    val passwordUpdateIsAllowed = password.isNotBlank() &&
            UserDataValidator.isValidPassword(newPassword) &&
            newPassword.trim() == confirmNewPassword.trim()

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.updating_your_password_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        PasswordUpdateScreen(
            onNavigateBack = {},
            currentPasswordState = passwordState,
            onCurrentPasswordChange = {},
            newPasswordState = newPasswordState,
            onNewPasswordChange = {},
            newPasswordConfirmationState = confirmNewPasswordState,
            onNewPasswordConfirmationChange = {},
            passwordUpdateIsAllowed = passwordUpdateIsAllowed,
            onUpdatePassword = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.updating_your_password_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.PasswordUpdated.toResultStateButton()
                    )
                }
            },
            onNavigateToRequestPasswordResetScreen = {},

            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onSuccessButton = { requestState = initialRequestState },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}
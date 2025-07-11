package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.DeleteAccountViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteAccountScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<DeleteAccountViewModel>()

    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val deletionIsAllowed by viewModel.deletionIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    DeleteAccountScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        deletionIsAllowed = deletionIsAllowed,
        onDeleteAccount = viewModel::deleteAccount,
        requestState = requestState,
        onCancelRequest = viewModel::cancelAccountDeletion,
        onSuccessButton = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = SettingsScreens.Start
            )
        },
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun DeleteAccountScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    deletionIsAllowed: Boolean,
    onDeleteAccount: () -> Unit,

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.DeleteUser,
        title = stringResource(R.string.delete_your_account_with_all_data),
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.delete_account),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = {
            GlassSurface(
                filledWidths = FilledWidthByScreenType(compact = .86f)
            ) {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    onDeleteAccount = onDeleteAccount
                )
            }
        },
        screenBottomContent = {
            PrimaryButton(
                text = stringResource(R.string.delete_account),
                enabled = deletionIsAllowed,
                gradientColor = GlanciColors.errorGradientPair,
                onClick = onDeleteAccount
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    onDeleteAccount: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            labelText = stringResource(R.string.password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
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
    val passwordState = ValidatedFieldState(
        fieldText = password,
        validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password).toUiStates()
    )
    val deletionIsAllowed = password.isNotBlank()

    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = null
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.deleting_your_account_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        DeleteAccountScreen(
            onNavigateBack = {},
            passwordState = passwordState,
            onPasswordChange = {},
            deletionIsAllowed = deletionIsAllowed,
            onDeleteAccount = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.deleting_your_account_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.AccountDeleted.toResultStateButton()
                    )
                }
            },

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
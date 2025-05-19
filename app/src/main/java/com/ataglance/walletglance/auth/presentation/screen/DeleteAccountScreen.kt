package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.auth.presentation.viewmodel.DeleteAccountViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainers.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithBackNavButtonTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.components.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteAccountScreenWrapper(
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<DeleteAccountViewModel>()

    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val deletionIsAllowed by viewModel.deletionIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    DeleteAccountScreen(
        onNavigateBack = navController::popBackStack,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        deletionIsAllowed = deletionIsAllowed,
        onDeleteAccount = viewModel::deleteAccount,
        requestState = requestState,
        onCancelRequest = viewModel::cancelAccountDeletion,
        onSuccessClose = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = SettingsScreens.Start
            )
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun DeleteAccountScreen(
    screenPadding: PaddingValues = PaddingValues(0.dp),
    onNavigateBack: () -> Unit,
    passwordState: ValidatedFieldUiState,
    onPasswordChange: (String) -> Unit,
    deletionIsAllowed: Boolean,
    onDeleteAccount: () -> Unit,
    requestState: RequestState?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    val backButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.profile_light_default,
        darkDefault = R.drawable.profile_dark_default,
    ).getByTheme(CurrAppTheme)

    if (requestState != null) {
        SetBackHandler()
    }

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessClose = onSuccessClose,
        onErrorClose = onErrorClose
    ) {
        ScreenContainerWithBackNavButtonTitleAndGlassSurface(
            onNavigateBack = onNavigateBack,
            backButtonText = stringResource(R.string.delete_account),
            backButtonImageRes = backButtonImageRes,
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
            onNavigateBack = {},
            passwordState = ValidatedFieldUiState(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password)
                    .toUiStates()
            ),
            onPasswordChange = {},
            deletionIsAllowed = password.isNotBlank(),
            onDeleteAccount = {},
            requestState = null,
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
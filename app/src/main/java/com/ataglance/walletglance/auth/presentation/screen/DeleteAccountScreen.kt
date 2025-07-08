package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.DeleteAccountViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithBackNavButtonTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
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
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    deletionIsAllowed: Boolean,
    onDeleteAccount: () -> Unit,
    requestState: RequestState<ButtonState>?,
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
                    gradientColor = GlanciColors.errorGradientPair,
                    onClick = onDeleteAccount
                )
            }
        )
    }
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

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        DeleteAccountScreen(
            onNavigateBack = {},
            passwordState = ValidatedFieldState(
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
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.RequestPasswordResetViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithBackNavButtonTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.request.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RequestPasswordResetScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<RequestPasswordResetViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.RequestPasswordReset>().email
        )
    }

    val emailState by viewModel.emailState.collectAsStateWithLifecycle()
    val requestIsAllowed by viewModel.requestIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    RequestPasswordResetScreen(
        screenPadding = screenPadding,
        emailState = emailState,
        onNavigateBack = navController::popBackStack,
        onEmailChange = viewModel::updateAndValidateEmail,
        requestIsAllowed = requestIsAllowed,
        onRequestPasswordReset = viewModel::requestPasswordReset,
        requestState = requestState,
        onCancelRequest = viewModel::cancelPasswordResetRequest,
        onSuccessClose = navController::popBackStack,
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun RequestPasswordResetScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    requestIsAllowed: Boolean,
    onRequestPasswordReset: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    val backButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.password_light_default,
        darkDefault = R.drawable.password_dark_default,
    ).get(CurrAppTheme)

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
            backButtonText = stringResource(R.string.reset_password),
            backButtonImageRes = backButtonImageRes,
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
    }
}

@Composable
private fun GlassSurfaceContent(
    emailState: ValidatedFieldState,
    onEmailChange: (String) -> Unit,
    onRequestPasswordReset: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = emailState,
            onValueChange = onEmailChange,
            labelText = stringResource(R.string.email),
            placeholderText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
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
            onNavigateBack = {},
            emailState = ValidatedFieldState(
                fieldText = email,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(email)
                    .toUiStates()
            ),
            onEmailChange = {},
            requestIsAllowed = true,
            onRequestPasswordReset = {},
            requestState = null,
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
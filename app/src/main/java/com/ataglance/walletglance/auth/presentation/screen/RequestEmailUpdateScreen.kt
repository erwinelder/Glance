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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.presentation.viewmodel.UpdateEmailViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithBackNavButtonTitleAndGlassSurface
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.errorHandling.presentation.component.field.SmallTextFieldWithLabelAndMessages
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun RequestEmailUpdateScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedKoinNavViewModel<UpdateEmailViewModel>(navController)

    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val newEmailState by viewModel.newEmailState.collectAsStateWithLifecycle()
    val emailUpdateIsAllowed by viewModel.emailUpdateIsAllowed.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    RequestEmailUpdateScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        passwordState = passwordState,
        onPasswordChange = viewModel::updateAndValidatePassword,
        newEmailState = newEmailState,
        onNewEmailChange = viewModel::updateAndValidateNewEmail,
        emailUpdateIsAllowed = emailUpdateIsAllowed,
        onRequestEmailUpdate = {
            job = coroutineScope.launch {
                if (!viewModel.requestEmailUpdate()) return@launch
                navViewModel.navigateToScreen(
                    navController = navController, screen = AuthScreens.EmailUpdateEmailVerification
                )
            }
        },
        requestState = requestState,
        onCancelRequest = {
            job?.cancel()
            job = null
            viewModel.resetRequestState()
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun RequestEmailUpdateScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldState,
    onNewEmailChange: (String) -> Unit,
    emailUpdateIsAllowed: Boolean,
    onRequestEmailUpdate: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onErrorClose: () -> Unit
) {
    val backButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.email_light_default,
        darkDefault = R.drawable.email_dark_default,
    ).get(CurrAppTheme)

    if (requestState != null) {
        SetBackHandler()
    }

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = onCancelRequest,
        onErrorClose = onErrorClose
    ) {
        ScreenContainerWithBackNavButtonTitleAndGlassSurface(
            onNavigateBack = onNavigateBack,
            backButtonText = stringResource(R.string.update_email),
            backButtonImageRes = backButtonImageRes,
            title = stringResource(R.string.update_your_email),
            glassSurfaceContent = {
                GlassSurfaceContent(
                    passwordState = passwordState,
                    onPasswordChange = onPasswordChange,
                    newEmailState = newEmailState,
                    onNewEmailChange = onNewEmailChange,
                    onRequestUpdateEmail = onRequestEmailUpdate
                )
            },
            bottomButtonBlock = {
                PrimaryButton(
                    text = stringResource(R.string.update_email),
                    enabled = emailUpdateIsAllowed,
                    onClick = onRequestEmailUpdate
                )
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    passwordState: ValidatedFieldState,
    onPasswordChange: (String) -> Unit,
    newEmailState: ValidatedFieldState,
    onNewEmailChange: (String) -> Unit,
    onRequestUpdateEmail: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        SmallTextFieldWithLabelAndMessages(
            state = passwordState,
            onValueChange = onPasswordChange,
            labelText = stringResource(R.string.current_password),
            placeholderText = stringResource(R.string.password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabelAndMessages(
            state = newEmailState,
            onValueChange = onNewEmailChange,
            labelText = stringResource(R.string.new_email),
            placeholderText = stringResource(R.string.email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Go,
            onGoKeyboardAction = onRequestUpdateEmail
        )
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun RequestEmailUpdateScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val newEmail = "newEmail@domain.com"

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        RequestEmailUpdateScreen(
            onNavigateBack = {},
            passwordState = ValidatedFieldState(),
            onPasswordChange = {},
            newEmailState = ValidatedFieldState(
                fieldText = newEmail,
                validationStates = UserDataValidator.validateEmail(newEmail).toUiStates()
            ),
            onNewEmailChange = {},
            emailUpdateIsAllowed = true,
            onRequestEmailUpdate = {},
            requestState = null,
            onCancelRequest = {},
            onErrorClose = {}
        )
    }
}
package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.mapperNew.toResultStateButton
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainer
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpEmailVerificationScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val signUpViewModel = backStack.sharedKoinNavViewModel<SignUpViewModel>(navController)

    val requestState by signUpViewModel.emailVerificationRequestState.collectAsStateWithLifecycle()

    SignUpEmailVerificationScreen(
        screenPadding = screenPadding,
        requestState = requestState,
        onCancelRequest = signUpViewModel::cancelEmailVerificationCheck,
        onSuccessButton = {
            if (signUpViewModel.isEmailVerified()) {
                navViewModel.navigateAndPopUpTo(
                    navController = navController,
                    screenToNavigateTo = SettingsScreens.Accounts,
                    inclusive = false
                )
            } else {
                signUpViewModel.checkEmailVerification()
            }
        },
        onErrorButton = signUpViewModel::resetEmailVerificationRequestState
    )
}

@Composable
fun SignUpEmailVerificationScreen(
    screenPadding: PaddingValues = PaddingValues(),

    requestState: RequestState<ButtonState, ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Email,
        title = "",
        requestStateButton = requestState,
        onCancelRequest = onCancelRequest,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton,
        screenCenterContent = {}
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun SignUpEmailVerificationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val coroutineScope = rememberCoroutineScope()
    var job = remember<Job?> { null }
    val initialRequestState = RequestState.Success<ButtonState, ButtonState>(
        state = AuthSuccess.SignUpEmailVerificationSent.toResultStateButton()
    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignUpEmailVerificationScreen(
            requestState = requestState,
            onCancelRequest = {
                requestState = initialRequestState
                job?.cancel()
            },
            onSuccessButton = {
                job = coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.checking_email_verification_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.SignedUp.toResultStateButton()
                    )
                }
            },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}
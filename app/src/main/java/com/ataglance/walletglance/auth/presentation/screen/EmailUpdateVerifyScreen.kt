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
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.auth.presentation.viewmodel.EmailUpdateVerifyViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainer
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EmailUpdateVerifyScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<EmailUpdateVerifyViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.VerifyEmailUpdate>().oobCode
        )
    }

    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    EmailUpdateVerifyScreen(
        screenPadding = screenPadding,
        requestState = requestState,
        onSuccessButton = {
            if (viewModel.isEmailVerified()) {
                navViewModel.navigateToScreenMovingTowardsRight(
                    navController = navController,
                    screen = MainScreens.Settings
                )
                navViewModel.setMoveScreensTowardsLeft(true)
            } else {
                viewModel.verifyEmail()
            }
        },
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun EmailUpdateVerifyScreen(
    screenPadding: PaddingValues = PaddingValues(),

    requestState: RequestState<ButtonState, ButtonState>?,
    onSuccessButton: () -> Unit,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Email,
        title = "",
        requestStateButton = requestState,
        onSuccessButton = onSuccessButton,
        onErrorButton = onErrorButton
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun EmailUpdateVerifyScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val coroutineScope = rememberCoroutineScope()
    val initialRequestState = RequestState.Success<ButtonState, ButtonState>(
        state = AuthSuccess.EmailUpdateVerificationCodeReceived.toResultStateButton()
    )
//    val initialRequestState = RequestState.Loading<ButtonState, ButtonState>(
//        messageRes = R.string.verifying_your_email_loader
//    )
    var requestState by remember {
        mutableStateOf<RequestState<ButtonState, ButtonState>>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        EmailUpdateVerifyScreen(
            requestState = requestState,
            onSuccessButton = {
                coroutineScope.launch {
                    requestState = RequestState.Loading(
                        messageRes = R.string.verifying_your_email_loader
                    )
                    delay(2000)
                    requestState = RequestState.Success(
                        state = AuthSuccess.EmailUpdated.toResultStateButton()
                    )
                }
            },
            onErrorButton = { requestState = initialRequestState }
        )
    }
}
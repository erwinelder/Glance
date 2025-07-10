package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.auth.presentation.viewmodel.VerifyEmailUpdateViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.icon.LargePrimaryIconWithMessage
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainer
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun VerifyEmailUpdateScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = koinViewModel<VerifyEmailUpdateViewModel> {
        parametersOf(
            backStack.toRoute<AuthScreens.VerifyEmailUpdate>().oobCode
        )
    }

    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    VerifyEmailUpdateScreen(
        screenPadding = screenPadding,
        onVerify = viewModel::verifyEmail,
        requestState = requestState,
        onSuccessClose = {
            navViewModel.navigateToScreenMovingTowardsRight(
                navController = navController,
                screen = MainScreens.Settings
            )
            navViewModel.setMoveScreensTowardsLeft(true)
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun VerifyEmailUpdateScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onVerify: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    SetBackHandler()

    AnimatedScreenWithRequestState(
        screenPadding = screenPadding,
        requestState = requestState,
        onSuccessClose = onSuccessClose,
        onErrorClose = onErrorClose
    ) {
        ScreenContainer {
            VerifyEmailPromptedComponent(onVerify = onVerify)
        }
    }
}

@Composable
private fun VerifyEmailPromptedComponent(onVerify: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LargePrimaryIconWithMessage(
            title = stringResource(R.string.verify_email),
            message = stringResource(R.string.email_update_verify_email_description),
            iconRes = R.drawable.email_large_icon,
            iconDescription = "Email sent icon"
        )
        SmallPrimaryButton(
            text = stringResource(R.string.verify),
            onClick = onVerify
        )
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun VerifyEmailUpdateScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        VerifyEmailUpdateScreen(
            onVerify = {},
            requestState = null,
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
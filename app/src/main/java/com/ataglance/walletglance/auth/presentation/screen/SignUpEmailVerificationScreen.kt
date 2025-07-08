package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.presentation.viewmodel.SignUpViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.container.icon.LargePrimaryIconWithMessage
import com.ataglance.walletglance.core.presentation.component.screenContainer.state.AnimatedScreenWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButton
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

@Composable
fun SignUpEmailVerificationScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedKoinNavViewModel<SignUpViewModel>(navController)

    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    SignUpEmailVerificationScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        onCheckEmailVerification = viewModel::checkEmailVerification,
        requestState = requestState,
        onCancelRequest = viewModel::cancelEmailVerificationCheck,
        onSuccessClose = {
            navViewModel.navigateAndPopUpTo(
                navController = navController,
                screenToNavigateTo = SettingsScreens.Accounts,
                inclusive = false
            )
        },
        onErrorClose = viewModel::resetRequestState
    )
}

@Composable
fun SignUpEmailVerificationScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onCheckEmailVerification: () -> Unit,
    requestState: RequestState<ButtonState>?,
    onCancelRequest: () -> Unit,
    onSuccessClose: () -> Unit,
    onErrorClose: () -> Unit
) {
    val backNavButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.email_light_default,
        darkDefault = R.drawable.email_dark_default,
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
        ScreenContainerWithTopBackNavButton(
            screenPadding = screenPadding,
            backNavButtonText = stringResource(R.string.email_verification),
            backNavButtonImageRes = backNavButtonImageRes,
            onBackNavButtonClick = onNavigateBack
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .weight(1f)
            ) {
                EmailVerificationPromptedComponent(onCheck = onCheckEmailVerification)
            }
        }
    }
}

@Composable
private fun EmailVerificationPromptedComponent(onCheck: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LargePrimaryIconWithMessage(
            title = stringResource(R.string.email_sent),
            message = stringResource(R.string.sign_up_email_verification_sent_message),
            iconRes = R.drawable.email_large_icon,
            iconDescription = "Email sent icon"
        )
        SmallPrimaryButton(
            text = stringResource(R.string.check_verification),
            onClick = onCheck
        )
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun SignUpEmailVerificationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SignUpEmailVerificationScreen(
            onNavigateBack = {},
            onCheckEmailVerification = {},
            requestState = null,
            onCancelRequest = {},
            onSuccessClose = {},
            onErrorClose = {}
        )
    }
}
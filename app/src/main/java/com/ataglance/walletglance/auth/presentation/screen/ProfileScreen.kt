package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.presentation.viewmodel.ProfileViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetDialogComponent
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.component.NavigateToSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.component.OpenSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val greetingsTitleRes by getGreetingsWidgetTitleRes()

    ProfileScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        greetingsTitle = stringResource(greetingsTitleRes),
        onSignOut = {
            viewModel.signOut()
            navController.popBackStack()
        },
        onNavigateToScreen = { screen ->
            navViewModel.navigateToScreen(navController, screen)
        },
        onPopBackStackAndNavigateToScreen = { screen ->
            navViewModel.popBackStackAndNavigate(navController, screen)
        }
    )
}

@Composable
fun ProfileScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    greetingsTitle: String,
    onSignOut: () -> Unit,
    onNavigateToScreen: (Any) -> Unit,
    onPopBackStackAndNavigateToScreen: (Any) -> Unit
) {
    val appTheme = CurrAppTheme

    var showSignOutSheet by remember { mutableStateOf(false) }


    Box {
        SettingsCategoryScreenContainer(
            screenPadding = screenPadding,
            thisCategory = SettingsCategory.Profile(appTheme),
            onNavigateBack = onNavigateBack,
            title = greetingsTitle,
            mainScreenContent = {
                NavigateToSettingsCategoryButton(SettingsCategory.DeleteAccount(appTheme), onPopBackStackAndNavigateToScreen)
                OpenSettingsCategoryButton(SettingsCategory.SignOut(appTheme)) { showSignOutSheet = true }
                NavigateToSettingsCategoryButton(SettingsCategory.UpdateEmail(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.UpdatePassword(appTheme), onPopBackStackAndNavigateToScreen)
//                NavigateToSettingsCategoryButton(SettingsCategory.ManageSubscriptions(appTheme), onNavigateToScreen)
            }
        )
        BottomSheetDialogComponent(
            visible = showSignOutSheet,
            iconRes = R.drawable.sign_out_large_icon,
            iconDescription = "Sing out",
            title = stringResource(R.string.sign_out),
            message = stringResource(R.string.sign_out_of_your_account),
            onDismissRequest = { showSignOutSheet = false }
        ) { onSheetHide ->
            PrimaryButton(
                text = stringResource(R.string.sign_out),
                onClick = {
                    onSheetHide()
                    showSignOutSheet = false
                    onSignOut()
                }
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ProfileScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        ProfileScreen(
            onNavigateBack = {},
            greetingsTitle = "Good afternoon, username!",
            onSignOut = {},
            onNavigateToScreen = {},
            onPopBackStackAndNavigateToScreen = {}
        )
    }
}
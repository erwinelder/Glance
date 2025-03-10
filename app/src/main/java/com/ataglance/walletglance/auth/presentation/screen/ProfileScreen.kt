package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheetDialog
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.settings.presentation.components.NavigateToSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.components.OpenSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainers.SettingsCategoryScreenContainer
import java.time.LocalDateTime

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateToScreen: (Any) -> Unit,
    onPopBackStackAndNavigateToScreen: (Any) -> Unit
) {
    val appTheme = CurrAppTheme
    val currentLocalDateTime = LocalDateTime.now()
    val greetingsTitleRes by remember(currentLocalDateTime.hour) {
        derivedStateOf {
            currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
        }
    }

    var showSignOutSheet by remember { mutableStateOf(false) }

    Box {
        SettingsCategoryScreenContainer(
            thisCategory = SettingsCategory.Profile(appTheme),
            onNavigateBack = onNavigateBack,
            title = stringResource(greetingsTitleRes),
            mainScreenContentBlock = {
                NavigateToSettingsCategoryButton(SettingsCategory.DeleteAccount(appTheme), onPopBackStackAndNavigateToScreen)
                OpenSettingsCategoryButton(SettingsCategory.SignOut(appTheme)) { showSignOutSheet = true }
                NavigateToSettingsCategoryButton(SettingsCategory.UpdateEmail(appTheme), onPopBackStackAndNavigateToScreen)
                NavigateToSettingsCategoryButton(SettingsCategory.UpdatePassword(appTheme), onPopBackStackAndNavigateToScreen)
//                NavigateToSettingsCategoryButton(SettingsCategory.ManageSubscriptions(appTheme), onNavigateToScreen)
            }
        )
        GlanceBottomSheetDialog(
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
    PreviewContainer(appTheme = appTheme) {
        ProfileScreen(
            onNavigateBack = {},
            onSignOut = {},
            onNavigateToScreen = {},
            onPopBackStackAndNavigateToScreen = {}
        )
    }
}
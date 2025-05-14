package com.ataglance.walletglance.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceNavigationButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun NavigateToSettingsCategoryButton(
    category: SettingsCategory,
    onNavigateToScreen: (Any) -> Unit
) {
    GlassSurfaceNavigationButton(
        text = stringResource(category.stringRes),
        imageRes = category.iconRes,
        onClick = {
            onNavigateToScreen(category.screen)
        }
    )
}
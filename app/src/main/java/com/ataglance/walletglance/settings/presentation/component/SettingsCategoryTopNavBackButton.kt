package com.ataglance.walletglance.settings.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun SettingsCategoryTopNavBackButton(
    category: SettingsCategory,
    onNavigateBack: () -> Unit
) {
    GlassSurfaceTopNavButtonBlock(
        text = stringResource(category.stringRes),
        imageRes = category.iconRes,
        onClick = onNavigateBack
    )
}
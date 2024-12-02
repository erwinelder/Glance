package com.ataglance.walletglance.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.components.buttons.GlassSurfaceNavigationButton
import com.ataglance.walletglance.settings.domain.SettingsCategory

@Composable
fun NavigateBackSettingsCategoryButton(
    category: SettingsCategory,
    onNavigateBack: () -> Unit
) {
    GlassSurfaceNavigationButton(
        text = stringResource(category.stringRes),
        imageRes = category.iconRes,
        showRightIconInsteadOfLeft = false,
        filledWidths = FilledWidthByScreenType(.96f),
        onClick = onNavigateBack
    )
}
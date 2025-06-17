package com.ataglance.walletglance.settings.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun NavigateBackSettingsCategoryButton(
    category: SettingsCategory,
    onNavigateBack: () -> Unit
) {
    GlassSurfaceTopNavButton(
        text = stringResource(category.stringRes),
        imageRes = category.iconRes,
        filledWidths = FilledWidthByScreenType(.96f),
        onClick = onNavigateBack
    )
}
package com.ataglance.walletglance.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.components.buttons.GlassSurfaceNavigationButton
import com.ataglance.walletglance.settings.domain.SettingsCategory

@Composable
fun OpenSettingsCategoryButton(
    category: SettingsCategory,
    onClick: () -> Unit
) {
    GlassSurfaceNavigationButton(
        text = stringResource(category.stringRes),
        imageRes = category.iconRes,
        rightIconRes = R.drawable.short_arrow_up_icon,
        onClick = onClick
    )
}
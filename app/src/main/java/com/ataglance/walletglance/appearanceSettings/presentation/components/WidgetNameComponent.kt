package com.ataglance.walletglance.appearanceSettings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.appearanceSettings.domain.utils.getLocalizedStringRes
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun WidgetNameComponent(widgetName: WidgetName, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(widgetName.getLocalizedStringRes()),
        fontSize = 20.sp,
        color = GlanceTheme.onSurface,
        modifier = modifier
    )
}
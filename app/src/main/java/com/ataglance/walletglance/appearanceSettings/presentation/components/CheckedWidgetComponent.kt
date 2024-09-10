package com.ataglance.walletglance.appearanceSettings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.appearanceSettings.domain.model.CheckedWidget
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.appearanceSettings.domain.utils.getLocalizedStringRes
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.TwoStateCheckboxContainer

@Composable
fun CheckedWidgetComponent(
    widget: CheckedWidget,
    modifier: Modifier = Modifier,
    onCheckedStateChange: (WidgetName, Boolean) -> Unit
) {
    TwoStateCheckboxContainer(
        checked = widget.isChecked,
        modifier = modifier,
        onClick = { isChecked ->
            onCheckedStateChange(widget.name, isChecked)
        }
    ) {
        Text(
            text = stringResource(widget.name.getLocalizedStringRes()),
            fontSize = 20.sp,
            color = GlanceTheme.onSurface,
            modifier = modifier
        )
    }
}
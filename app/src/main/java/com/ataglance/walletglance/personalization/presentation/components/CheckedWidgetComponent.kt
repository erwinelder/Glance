package com.ataglance.walletglance.personalization.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.component.checkboxes.TwoStateCheckboxContainer
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.utils.getLocalizedStringRes

@Composable
fun RowScope.CheckedWidgetComponent(
    widget: CheckedWidget,
    onCheckedStateChange: (WidgetName, Boolean) -> Unit
) {
    TwoStateCheckboxContainer(
        checked = widget.isChecked,
        onClick = { isChecked ->
            onCheckedStateChange(widget.name, isChecked)
        },
        modifier = Modifier.weight(1f, fill = false)
    ) {
        Text(
            text = stringResource(widget.name.getLocalizedStringRes()),
            fontSize = 20.sp,
            color = GlanceColors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
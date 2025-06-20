package com.ataglance.walletglance.core.presentation.component.widget.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetTitleComponent

@Composable
fun WidgetContainer(
    title: String,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    buttonsBlock: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        GlassSurface(filledWidths = filledWidths) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            ) {
                WidgetTitleComponent(title = title)
                content()
            }
        }
        if (buttonsBlock != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = buttonsBlock
            )
        }
    }
}
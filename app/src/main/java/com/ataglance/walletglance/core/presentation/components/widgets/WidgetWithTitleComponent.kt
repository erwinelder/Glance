package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType

@Composable
fun WidgetWithTitleComponent(
    filledWidthByScreenType: FilledWidthByScreenType = FilledWidthByScreenType(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    WidgetComponent(filledWidthByScreenType = filledWidthByScreenType) {
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
}
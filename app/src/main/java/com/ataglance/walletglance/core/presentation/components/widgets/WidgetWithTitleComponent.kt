package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.GlanceTheme

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
            Text(
                text = title,
                color = GlanceTheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
            content()
        }
    }
}
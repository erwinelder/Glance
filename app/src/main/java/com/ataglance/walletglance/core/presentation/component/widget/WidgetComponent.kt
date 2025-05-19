package com.ataglance.walletglance.core.presentation.component.widget

import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface

@Composable
fun WidgetComponent(
    filledWidthByScreenType: FilledWidthByScreenType = FilledWidthByScreenType(),
    content: @Composable () -> Unit
) {
    GlassSurface(
        filledWidths = filledWidthByScreenType
    ) {
        content()
    }
}
package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface

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
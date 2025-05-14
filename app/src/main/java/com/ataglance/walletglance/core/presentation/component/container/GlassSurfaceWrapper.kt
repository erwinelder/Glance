package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType

@Composable
fun GlassSurfaceWrapper(
    modifier: Modifier = Modifier,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(),
    cornerSize: Dp = dimensionResource(R.dimen.widget_corner_size),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    columnWrapperModifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier,
        filledWidths = filledWidths,
        cornerSize = cornerSize
    ) {
        GlassSurfaceContentColumnWrapper(
            verticalArrangement = verticalArrangement,
            modifier = columnWrapperModifier,
            content = content
        )
    }
}
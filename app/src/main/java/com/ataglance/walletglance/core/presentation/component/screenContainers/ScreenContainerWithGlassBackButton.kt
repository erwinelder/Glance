package com.ataglance.walletglance.core.presentation.component.screenContainers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceNavigationButton
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.utils.add

@Composable
fun ScreenContainerWithGlassBackButton(
    screenPadding: PaddingValues = PaddingValues(0.dp),
    padding: PaddingValues = PaddingValues(vertical = 8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(24.dp),
    gap: Dp = 24.dp,
    onNavigateBack: () -> Unit,
    backButtonText: String,
    @DrawableRes backButtonImageRes: Int? = null,
    contentFilledWith: FilledWidthByScreenType? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(gap),
        modifier = modifier
            .padding(padding.add(screenPadding))
            .fillMaxSize()
    ) {
        GlassSurfaceNavigationButton(
            text = backButtonText,
            imageRes = backButtonImageRes,
            showRightIconInsteadOfLeft = false,
            filledWidths = FilledWidthByScreenType(.96f, .75f, .54f),
            onClick = onNavigateBack
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement,
            modifier = Modifier
                .fillMaxWidth(contentFilledWith?.getByType(CurrWindowType) ?: 1f)
                .weight(1f)
        ) {
            content()
        }
    }
}
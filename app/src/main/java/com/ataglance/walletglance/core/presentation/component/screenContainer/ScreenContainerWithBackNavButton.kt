package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.GlassSurfaceTopNavButtonBlock
import com.ataglance.walletglance.core.presentation.utils.plus

@Composable
fun ScreenContainerWithTopBackNavButton(
    screenPadding: PaddingValues = PaddingValues(),
    bottomPadding: Dp = 16.dp,
    backNavButtonText: String,
    backNavButtonImageRes: Int? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = screenPadding + PaddingValues(top = 8.dp, bottom = bottomPadding)
            )
    ) {

        GlassSurfaceTopNavButtonBlock(
            text = backNavButtonText,
            imageRes = backNavButtonImageRes,
            filledWidths = FilledWidthByScreenType(.96f, .75f, .75f),
            onClick = onBackNavButtonClick,
            companionComponent = backNavButtonCompanionComponent
        )

        content()

    }
}

@Composable
fun ScreenContainerWithTopBackNavButton(
    screenPadding: PaddingValues = PaddingValues(),
    bottomPadding: Dp = 16.dp,
    backNavButtonText: String,
    backNavButtonIconComponent: @Composable (() -> Unit)? = null,
    onBackNavButtonClick: () -> Unit,
    backNavButtonCompanionComponent: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = screenPadding + PaddingValues(top = 8.dp, bottom = bottomPadding)
            )
    ) {

        GlassSurfaceTopNavButtonBlock(
            text = backNavButtonText,
            iconComponent = backNavButtonIconComponent,
            filledWidths = FilledWidthByScreenType(.96f, .75f, .75f),
            onClick = onBackNavButtonClick,
            companionComponent = backNavButtonCompanionComponent
        )

        content()

    }
}
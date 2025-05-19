package com.ataglance.walletglance.core.presentation.component.widget

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType

@Composable
fun WidgetWithTitleAndButtonComponent(
    filledWidthByScreenType: FilledWidthByScreenType = FilledWidthByScreenType(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    title: String,
    bottomNavigationText: String = stringResource(R.string.view_all),
    onBottomNavigationButtonClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    WidgetContainerWithButton(
        bottomNavigationText = bottomNavigationText,
        onBottomNavigationButtonClick = onBottomNavigationButtonClick
    ) {
        WidgetWithTitleComponent(
            filledWidthByScreenType = filledWidthByScreenType,
            contentPadding = contentPadding,
            title = title,
            content = content
        )
    }
}
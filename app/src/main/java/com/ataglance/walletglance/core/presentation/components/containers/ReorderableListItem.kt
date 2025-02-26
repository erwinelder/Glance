package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun LazyItemScope.ReorderableListItem(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillParentMaxWidth(),
    ) {
        content()
        Icon(
            painter = painterResource(R.drawable.reorder_icon),
            contentDescription = "reorder",
            tint = GlanceColors.outline,
            modifier = Modifier
                .size(32.dp)
                .padding(start = 8.dp)
        )
    }
}
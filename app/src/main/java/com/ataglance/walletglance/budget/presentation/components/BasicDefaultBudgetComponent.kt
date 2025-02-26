package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.category.presentation.components.CategoryIconComponent
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.utils.formatWithSpaces

@Composable
fun BasicDefaultBudgetComponent(
    budget: Budget,
    onClick: (Budget) -> Unit,
    modifier: Modifier = Modifier,
    clickEnabled: Boolean = true,
    topRightComponent: @Composable (() -> Unit)? = null
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(budget) },
        clickEnabled = clickEnabled,
        filledWidth = 1f,
        paddingValues = PaddingValues(24.dp, 16.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                budget.category?.let {
                    CategoryIconComponent(category = it)
                }
                Text(
                    text = budget.name,
                    color = GlanceColors.onSurface,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                topRightComponent?.let { it() }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.limit) + ":",
                    color = GlanceColors.outline,
                    fontSize = 18.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = budget.amountLimit.formatWithSpaces(),
                        color = GlanceColors.onSurface,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = budget.currency,
                        color = GlanceColors.onSurface.copy(.6f),
                        fontSize = 19.sp
                    )
                }
            }
        }
    }
}
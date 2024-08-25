package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.budget.domain.Budget
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.category.presentation.components.CategoryIconComponent
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface

@Composable
fun EditingBudgetComponent(
    appTheme: AppTheme?,
    budget: Budget,
    onClick: (Budget) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(budget) },
        filledWidth = 1f,
        paddingValues = PaddingValues(24.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                budget.category?.let {
                    CategoryIconComponent(category = it, appTheme = appTheme)
                }
                Text(
                    text = budget.name,
                    color = GlanceTheme.onSurface,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    painter = painterResource(R.drawable.short_arrow_right_icon),
                    contentDescription = "right arrow icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.limit) + ":",
                    color = GlanceTheme.outline,
                    fontSize = 18.sp
                )
                Text(
                    text = budget.amountLimit.formatWithSpaces(),
                    color = GlanceTheme.onSurface,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = budget.currency,
                    color = GlanceTheme.onSurface,
                    fontSize = 19.sp
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun EditingBudgetComponentPreview() {
    EditBudgetsScreenPreview()
}
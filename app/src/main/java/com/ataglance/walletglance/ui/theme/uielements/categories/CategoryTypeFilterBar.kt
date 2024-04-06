package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton

@Composable
fun CategoryTypeFilterBar(
    currentCategoryType: CategoryType,
    onClick: (CategoryType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(if (WindowTypeIsCompact) .82f else .56f)
    ) {
        BarButton(
            onClick = { onClick(CategoryType.Income) },
            active = currentCategoryType == CategoryType.Income,
            text = stringResource(R.string.income_plural)
        )
        BarButton(
            onClick = { onClick(CategoryType.Expense) },
            active = currentCategoryType == CategoryType.Expense,
            text = stringResource(R.string.expenses)
        )
    }
}
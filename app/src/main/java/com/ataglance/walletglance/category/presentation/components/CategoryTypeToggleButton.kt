package com.ataglance.walletglance.category.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.utils.toggle
import com.ataglance.walletglance.core.presentation.components.buttons.TypeToggleButton

@Composable
fun CategoryTypeToggleButton(
    currentType: CategoryType,
    onClick: (CategoryType) -> Unit
) {
    val textRes = when (currentType) {
        CategoryType.Expense -> R.string.expenses
        CategoryType.Income -> R.string.income_plural
    }

    TypeToggleButton(text = stringResource(textRes)) {
        onClick(currentType.toggle())
    }
}
package com.ataglance.walletglance.category.presentation.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.utils.toggle
import com.ataglance.walletglance.core.presentation.components.buttons.TypeToggleButton

@Composable
fun CategoryTypeToggleButton(
    currentType: CategoryType,
    @StringRes expenseTextRes: Int = R.string.expenses,
    @StringRes incomeTextRes: Int = R.string.income_plural,
    onClick: (CategoryType) -> Unit
) {
    val textRes = when (currentType) {
        CategoryType.Expense -> expenseTextRes
        CategoryType.Income -> incomeTextRes
    }

    TypeToggleButton(text = stringResource(textRes)) {
        onClick(currentType.toggle())
    }
}
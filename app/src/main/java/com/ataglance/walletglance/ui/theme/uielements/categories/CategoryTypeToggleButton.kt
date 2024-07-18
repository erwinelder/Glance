package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.ui.theme.uielements.buttons.TypeToggleButton
import com.ataglance.walletglance.data.utils.toggle

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
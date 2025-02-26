package com.ataglance.walletglance.personalization.presentation.utils

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.personalization.domain.model.WidgetName


@StringRes
fun WidgetName.getLocalizedStringRes(): Int {
    return when (this) {
        WidgetName.ChosenBudgets -> R.string.chosen_budgets
        WidgetName.TotalForPeriod -> R.string.total_for_period
        WidgetName.RecentRecords -> R.string.recent_records
        WidgetName.TopExpenseCategories -> R.string.top_expense_categories
    }
}

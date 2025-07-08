package com.ataglance.walletglance.budget.mapper.budgetOnWidget

import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel


fun List<BudgetOnWidgetDataModel>.toIntList(): List<Int> {
    return map { it.budgetId }
}

fun List<Int>.toBudgetOnWidgetDataModels(): List<BudgetOnWidgetDataModel> {
    return map { BudgetOnWidgetDataModel(budgetId = it) }
}

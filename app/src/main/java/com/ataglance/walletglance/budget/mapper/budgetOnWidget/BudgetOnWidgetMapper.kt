package com.ataglance.walletglance.budget.mapper.budgetOnWidget

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity


fun List<BudgetOnWidgetEntity>.toIntList(): List<Int> {
    return map { it.budgetId }
}

fun List<Int>.toBudgetOnWidgetEntities(): List<BudgetOnWidgetEntity> {
    return map { BudgetOnWidgetEntity(budgetId = it) }
}

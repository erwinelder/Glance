package com.ataglance.walletglance.personalization.mapper

import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity


fun List<BudgetOnWidgetEntity>.toIntList(): List<Int> {
    return map { it.budgetId }
}

fun List<Int>.toBudgetOnWidgetEntities(): List<BudgetOnWidgetEntity> {
    return map { BudgetOnWidgetEntity(budgetId = it) }
}

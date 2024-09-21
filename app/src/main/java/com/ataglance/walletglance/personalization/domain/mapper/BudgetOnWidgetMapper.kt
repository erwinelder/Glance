package com.ataglance.walletglance.personalization.domain.mapper

import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity


fun List<BudgetOnWidgetEntity>.toIntList(): List<Int> {
    return this.map { it.budgetId }
}


fun List<Int>.toEntityList(): List<BudgetOnWidgetEntity> {
    return this.map { BudgetOnWidgetEntity(budgetId = it) }
}

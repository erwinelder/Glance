package com.ataglance.walletglance.personalization.mapper

import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity



fun Map<String, Any?>.toBudgetOnWidgetEntity(): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(budgetId = this["budgetId"] as Int)
}

fun BudgetOnWidgetEntity.toMap(timestamp: Long): HashMap<String, Any> {
    return hashMapOf(
        "LMT" to timestamp,
        "budgetId" to budgetId
    )
}



fun List<BudgetOnWidgetEntity>.toIntList(): List<Int> {
    return this.map { it.budgetId }
}



fun List<Int>.toEntityList(): List<BudgetOnWidgetEntity> {
    return this.map { BudgetOnWidgetEntity(budgetId = it) }
}

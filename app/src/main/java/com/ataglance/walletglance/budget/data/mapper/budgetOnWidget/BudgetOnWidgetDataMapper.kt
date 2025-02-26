package com.ataglance.walletglance.budget.data.mapper.budgetOnWidget

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity
import com.ataglance.walletglance.core.utils.convertToIntOrZero


fun BudgetOnWidgetEntity.toRemoteEntity(
    updateTime: Long,
    deleted: Boolean
): BudgetOnWidgetRemoteEntity {
    return BudgetOnWidgetRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        budgetId = budgetId
    )
}

fun BudgetOnWidgetRemoteEntity.toLocalEntity(): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(
        budgetId = budgetId
    )
}


fun BudgetOnWidgetRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "budgetId" to budgetId
    )
}

fun Map<String, Any?>.toBudgetOnWidgetRemoteEntity(): BudgetOnWidgetRemoteEntity {
    return BudgetOnWidgetRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        budgetId = this["budgetId"].convertToIntOrZero()
    )
}
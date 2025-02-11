package com.ataglance.walletglance.personalization.data.mapper

import com.ataglance.walletglance.core.utils.convertToIntOrZero
import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.data.remote.model.BudgetOnWidgetRemoteEntity


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
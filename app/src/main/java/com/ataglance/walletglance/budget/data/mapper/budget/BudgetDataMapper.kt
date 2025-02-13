package com.ataglance.walletglance.budget.data.mapper.budget

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.core.utils.convertToDoubleOrZero
import com.ataglance.walletglance.core.utils.convertToIntOrZero


fun BudgetEntity.toRemoteEntity(updateTime: Long, deleted: Boolean): BudgetRemoteEntity {
    return BudgetRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}

fun BudgetRemoteEntity.toLocalEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}


fun BudgetAccountAssociation.toRemoteAssociation(
    updateTime: Long,
    deleted: Boolean
): BudgetAccountRemoteAssociation {
    return BudgetAccountRemoteAssociation(
        updateTime = updateTime,
        deleted = deleted,
        budgetId = budgetId,
        accountId = accountId
    )
}

fun BudgetAccountRemoteAssociation.toLocalAssociation(): BudgetAccountAssociation {
    return BudgetAccountAssociation(
        budgetId = budgetId,
        accountId = accountId
    )
}


fun BudgetRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "id" to id,
        "amountLimit" to amountLimit,
        "categoryId" to categoryId,
        "name" to name,
        "repeatingPeriod" to repeatingPeriod,
    )
}

fun Map<String, Any?>.toBudgetRemoteEntity(): BudgetRemoteEntity {
    return BudgetRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        id = this["id"].convertToIntOrZero(),
        amountLimit = this["amountLimit"].convertToDoubleOrZero(),
        categoryId = this["categoryId"].convertToIntOrZero(),
        name = this["name"] as String,
        repeatingPeriod = this["repeatingPeriod"] as String,
    )
}


fun BudgetAccountRemoteAssociation.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
        "budgetId" to budgetId,
        "accountId" to accountId
    )
}

fun Map<String, Any?>.toBudgetAccountRemoteAssociation(): BudgetAccountRemoteAssociation {
    return BudgetAccountRemoteAssociation(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        budgetId = this["budgetId"].convertToIntOrZero(),
        accountId = this["accountId"].convertToIntOrZero()
    )
}

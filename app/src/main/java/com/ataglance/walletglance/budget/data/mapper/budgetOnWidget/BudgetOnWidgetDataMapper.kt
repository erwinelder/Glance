package com.ataglance.walletglance.budget.data.mapper.budgetOnWidget

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto


fun BudgetOnWidgetDataModel.toEntity(timestamp: Long, deleted: Boolean): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetOnWidgetEntity.toDataModel(): BudgetOnWidgetDataModel {
    return BudgetOnWidgetDataModel(
        budgetId = budgetId
    )
}

fun BudgetOnWidgetDataModel.toDto(timestamp: Long, deleted: Boolean): BudgetOnWidgetDto {
    return BudgetOnWidgetDto(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetOnWidgetEntity.toDto(): BudgetOnWidgetDto {
    return BudgetOnWidgetDto(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetOnWidgetDto.toEntity(): BudgetOnWidgetEntity {
    return BudgetOnWidgetEntity(
        budgetId = budgetId,
        timestamp = timestamp,
        deleted = deleted
    )
}

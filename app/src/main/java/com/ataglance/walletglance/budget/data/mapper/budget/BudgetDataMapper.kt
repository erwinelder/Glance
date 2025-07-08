package com.ataglance.walletglance.budget.data.mapper.budget

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociationEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociationDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountAssociationDto
import com.ataglance.walletglance.budget.data.remote.model.BudgetDto
import com.ataglance.walletglance.budget.data.remote.model.BudgetDtoWithAssociations


fun BudgetDataModel.withAssociations(
    associations: List<BudgetAccountAssociationDataModel> = emptyList()
): BudgetDataModelWithAssociations {
    return BudgetDataModelWithAssociations(
        budget = this,
        associations = associations
    )
}


fun BudgetDataModel.toEntity(timestamp: Long, deleted: Boolean): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationDataModel.toEntity(): BudgetAccountAssociationEntity {
    return BudgetAccountAssociationEntity(budgetId = budgetId, accountId = accountId)
}

fun BudgetDataModelWithAssociations.toEntityWithAssociations(
    timestamp: Long,
    deleted: Boolean
): BudgetEntityWithAssociations {
    return BudgetEntityWithAssociations(
        budget = budget.toEntity(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toEntity() }
    )
}


fun BudgetEntity.toDataModel(): BudgetDataModel {
    return BudgetDataModel(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod
    )
}

fun BudgetAccountAssociationEntity.toDataModel(): BudgetAccountAssociationDataModel {
    return BudgetAccountAssociationDataModel(budgetId = budgetId, accountId = accountId)
}

fun BudgetEntityWithAssociations.toDataModelWithAssociations(): BudgetDataModelWithAssociations {
    return BudgetDataModelWithAssociations(
        budget = budget.toDataModel(),
        associations = associations.map { it.toDataModel() }
    )
}


fun BudgetDataModel.toDto(timestamp: Long, deleted: Boolean): BudgetDto {
    return BudgetDto(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationDataModel.toDto(): BudgetAccountAssociationDto {
    return BudgetAccountAssociationDto(budgetId = budgetId, accountId = accountId)
}

fun BudgetDataModelWithAssociations.toDtoWithAssociations(
    timestamp: Long,
    deleted: Boolean
): BudgetDtoWithAssociations {
    return BudgetDtoWithAssociations(
        budget = budget.toDto(timestamp = timestamp, deleted = deleted),
        associations = associations.map { it.toDto() }
    )
}


fun BudgetEntity.toDto(): BudgetDto {
    return BudgetDto(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationEntity.toDto(): BudgetAccountAssociationDto {
    return BudgetAccountAssociationDto(budgetId = budgetId, accountId = accountId)
}

fun BudgetEntityWithAssociations.toDtoWithAssociations(): BudgetDtoWithAssociations {
    return BudgetDtoWithAssociations(
        budget = budget.toDto(),
        associations = associations.map { it.toDto() }
    )
}


fun BudgetDto.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        amountLimit = amountLimit,
        categoryId = categoryId,
        name = name,
        repeatingPeriod = repeatingPeriod,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun BudgetAccountAssociationDto.toEntity(): BudgetAccountAssociationEntity {
    return BudgetAccountAssociationEntity(budgetId = budgetId, accountId = accountId)
}

fun BudgetDtoWithAssociations.toEntityWithAssociations(): BudgetEntityWithAssociations {
    return BudgetEntityWithAssociations(
        budget = budget.toEntity(),
        associations = associations.map { it.toEntity() }
    )
}

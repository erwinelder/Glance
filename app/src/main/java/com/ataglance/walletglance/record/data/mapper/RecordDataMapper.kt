package com.ataglance.walletglance.record.data.mapper

import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.model.RecordItemEntity
import com.ataglance.walletglance.record.data.model.RecordDataModel
import com.ataglance.walletglance.record.data.model.RecordDataModelWithItems
import com.ataglance.walletglance.record.data.model.RecordItemDataModel
import com.ataglance.walletglance.record.data.remote.model.RecordCommandDto
import com.ataglance.walletglance.record.data.remote.model.RecordCommandDtoWithItems
import com.ataglance.walletglance.record.data.remote.model.RecordItemDto
import com.ataglance.walletglance.record.data.remote.model.RecordQueryDto
import com.ataglance.walletglance.record.data.remote.model.RecordQueryDtoWithItems


fun RecordDataModel.toEntity(timestamp: Long, deleted: Boolean): RecordEntity {
    return RecordEntity(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDataModel.toEntity(): RecordItemEntity {
    return RecordItemEntity(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordDataModelWithItems.toEntityWithItems(
    timestamp: Long,
    deleted: Boolean
): RecordEntityWithItems {
    return RecordEntityWithItems(
        record = record.toEntity(timestamp = timestamp, deleted = deleted),
        items = items.map { it.toEntity() }
    )
}


fun RecordEntity.toDataModel(): RecordDataModel {
    return RecordDataModel(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets
    )
}

fun RecordItemEntity.toDataModel(): RecordItemDataModel {
    return RecordItemDataModel(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordEntityWithItems.toDataModelWithItems(): RecordDataModelWithItems {
    return RecordDataModelWithItems(
        record = record.toDataModel(),
        items = items.map { it.toDataModel() }
    )
}


fun RecordDataModel.toCommandDto(timestamp: Long, deleted: Boolean): RecordCommandDto {
    return RecordCommandDto(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDataModel.toDto(): RecordItemDto {
    return RecordItemDto(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordDataModelWithItems.toCommandDtoWithItems(
    timestamp: Long,
    deleted: Boolean
): RecordCommandDtoWithItems {
    return RecordCommandDtoWithItems(
        record = record.toCommandDto(timestamp = timestamp, deleted = deleted),
        items = items.map { it.toDto() }
    )
}


fun RecordEntity.toCommandDto(): RecordCommandDto {
    return RecordCommandDto(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemEntity.toDto(): RecordItemDto {
    return RecordItemDto(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordEntityWithItems.toCommandDtoWithItems(): RecordCommandDtoWithItems {
    return RecordCommandDtoWithItems(
        record = record.toCommandDto(),
        items = items.map { it.toDto() }
    )
}


fun RecordQueryDto.toEntity(): RecordEntity {
    return RecordEntity(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun RecordItemDto.toEntity(): RecordItemEntity {
    return RecordItemEntity(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordQueryDtoWithItems.toEntityWithItems(): RecordEntityWithItems {
    return RecordEntityWithItems(
        record = record.toEntity(),
        items = items.map { it.toEntity() }
    )
}

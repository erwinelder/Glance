package com.ataglance.walletglance.transfer.data.mapper

import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import com.ataglance.walletglance.transfer.data.model.TransferDataModel
import com.ataglance.walletglance.transfer.data.remote.model.TransferCommandDto
import com.ataglance.walletglance.transfer.data.remote.model.TransferQueryDto


fun TransferDataModel.toEntity(timestamp: Long, deleted: Boolean): TransferEntity {
    return TransferEntity(
        id = id,
        date = date,
        senderAccountId = senderAccountId,
        receiverAccountId = receiverAccountId,
        senderAmount = senderAmount,
        receiverAmount = receiverAmount,
        senderRate = senderRate,
        receiverRate = receiverRate,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun TransferEntity.toDataModel(): TransferDataModel {
    return TransferDataModel(
        id = id,
        date = date,
        senderAccountId = senderAccountId,
        receiverAccountId = receiverAccountId,
        senderAmount = senderAmount,
        receiverAmount = receiverAmount,
        senderRate = senderRate,
        receiverRate = receiverRate,
        includeInBudgets = includeInBudgets
    )
}

fun TransferDataModel.toCommandDto(timestamp: Long, deleted: Boolean): TransferCommandDto {
    return TransferCommandDto(
        id = id,
        date = date,
        senderAccountId = senderAccountId,
        receiverAccountId = receiverAccountId,
        senderAmount = senderAmount,
        receiverAmount = receiverAmount,
        senderRate = senderRate,
        receiverRate = receiverRate,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun TransferEntity.toCommandDto(): TransferCommandDto {
    return TransferCommandDto(
        id = id,
        date = date,
        senderAccountId = senderAccountId,
        receiverAccountId = receiverAccountId,
        senderAmount = senderAmount,
        receiverAmount = receiverAmount,
        senderRate = senderRate,
        receiverRate = receiverRate,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun TransferQueryDto.toEntity(): TransferEntity {
    return TransferEntity(
        id = id,
        date = date,
        senderAccountId = senderAccountId,
        receiverAccountId = receiverAccountId,
        senderAmount = senderAmount,
        receiverAmount = receiverAmount,
        senderRate = senderRate,
        receiverRate = receiverRate,
        includeInBudgets = includeInBudgets,
        timestamp = timestamp,
        deleted = deleted
    )
}
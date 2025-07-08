package com.ataglance.walletglance.account.data.mapper

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.data.remote.model.AccountCommandDto
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto


fun AccountDataModel.toEntity(timestamp: Long, deleted: Boolean): AccountEntity {
    return AccountEntity(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun AccountEntity.toDataModel(): AccountDataModel {
    return AccountDataModel(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance
    )
}

fun AccountDataModel.toCommandDto(timestamp: Long, deleted: Boolean): AccountCommandDto {
    return AccountCommandDto(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun AccountEntity.toCommandDto(): AccountCommandDto {
    return AccountCommandDto(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        timestamp = timestamp,
        deleted = deleted
    )
}

fun AccountQueryDto.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        timestamp = timestamp,
        deleted = deleted
    )
}

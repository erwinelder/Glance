package com.ataglance.walletglance.account.mapper

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors


fun AccountEntity.toDomainModel(): Account {
    return Account(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = AccountColors.fromName(color),
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        isActive = isActive
    )
}

fun Account.toDataModel(): AccountEntity {
    return AccountEntity(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color.getNameValue(),
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        isActive = isActive
    )
}

package com.ataglance.walletglance.data.mappers

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.color.AccountColorWithName
import com.ataglance.walletglance.data.local.entities.AccountEntity



fun AccountEntity.toDomainModel(accountColorProvider: (String) -> AccountColorWithName): Account {
    return Account(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = accountColorProvider(color),
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        isActive = isActive
    )
}

fun List<AccountEntity>.toDomainModels(
    accountColorProvider: (String) -> AccountColorWithName
): List<Account> {
    return this.map {
        it.toDomainModel(accountColorProvider = accountColorProvider)
    }
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

fun List<Account>.toDataModels(): List<AccountEntity> {
    return this.map { it.toDataModel() }
}
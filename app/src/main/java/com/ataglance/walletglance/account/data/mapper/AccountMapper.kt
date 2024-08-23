package com.ataglance.walletglance.account.data.mapper

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountColorWithName
import com.ataglance.walletglance.account.data.local.model.AccountEntity



fun AccountEntity.toAccount(accountColorProvider: (String) -> AccountColorWithName): Account {
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

fun List<AccountEntity>.toAccountList(
    accountColorProvider: (String) -> AccountColorWithName
): List<Account> {
    return this.map {
        it.toAccount(accountColorProvider = accountColorProvider)
    }
}



fun Account.toAccountEntity(): AccountEntity {
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

fun List<Account>.toAccountEntityList(): List<AccountEntity> {
    return this.map { it.toAccountEntity() }
}
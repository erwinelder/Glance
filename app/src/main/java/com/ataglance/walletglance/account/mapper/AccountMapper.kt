package com.ataglance.walletglance.account.mapper

import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountColorWithName
import com.ataglance.walletglance.core.utils.convertToDoubleOrZero


fun Map<String, Any?>.toAccountEntity(): AccountEntity {
    return AccountEntity(
        id = (this["id"] as Long).toInt(),
        orderNum = (this["orderNum"] as Long).toInt(),
        name = this["name"] as String,
        currency = this["currency"] as String,
        balance = this["balance"].convertToDoubleOrZero(),
        color = this["color"] as String,
        hide = this["hide"] as Boolean,
        hideBalance = this["hideBalance"] as Boolean,
        withoutBalance = this["withoutBalance"] as Boolean,
        isActive = this["isActive"] as Boolean
    )
}

fun AccountEntity.toMap(timestamp: Long): HashMap<String, Any> {
    return hashMapOf(
        "LMT" to timestamp,
        "id" to this.id,
        "orderNum" to this.orderNum,
        "name" to this.name,
        "currency" to this.currency,
        "balance" to this.balance,
        "color" to this.color,
        "hide" to this.hide,
        "hideBalance" to this.hideBalance,
        "withoutBalance" to this.withoutBalance,
        "isActive" to this.isActive
    )
}



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

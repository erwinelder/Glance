package com.ataglance.walletglance.account.mapper

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.model.AccountDraft
import java.util.Locale


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
        withoutBalance = withoutBalance
    )
}

fun Account.toDataModel(timestamp: Long): AccountEntity { // TODO-SYNC: pass timestamp from the caller
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
        timestamp = timestamp
    )
}


fun Account.toDraft(): AccountDraft {
    return AccountDraft(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = "%.2f".format(Locale.US, balance),
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        isActive = isActive
    )
}

fun AccountDraft.toAccount(): Account? {
    val balance = balance.toDoubleOrNull() ?: return null

    return Account(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        isActive = isActive
    )
}
package com.ataglance.walletglance.account.mapper

import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.model.AccountDraft
import java.util.Locale


fun AccountDataModel.toDomainModel(isActive: Boolean = false): Account {
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

fun List<AccountDataModel>.toDomainModels(): List<Account> {
    return mapIndexed { index, account ->
        account.toDomainModel(isActive = index == 0)
    }
}


fun Account.toDataModel(): AccountDataModel {
    return AccountDataModel(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color.getNameValue(),
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance
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
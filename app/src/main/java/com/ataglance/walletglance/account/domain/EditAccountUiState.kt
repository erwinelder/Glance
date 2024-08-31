package com.ataglance.walletglance.account.domain

import com.ataglance.walletglance.account.domain.color.AccountColorWithName
import com.ataglance.walletglance.account.domain.color.AccountColors
import com.ataglance.walletglance.account.utils.toAccountColorWithName

data class EditAccountUiState(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "",
    val currency: String = "",
    val balance: String = "0.00",
    val color: AccountColorWithName = AccountColors.Default.toAccountColorWithName(),
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false
) {

    fun allowSaving(): Boolean {
        return name.isNotBlank() &&
                currency.isNotBlank() &&
                balance.isNotBlank() &&
                balance.last() != '.'
    }

    fun toAccount(): Account {
        return Account(
            id = id,
            orderNum = orderNum,
            name = name,
            currency = currency,
            balance = balance.toDouble(),
            color = color,
            hide = hide,
            hideBalance = hideBalance,
            withoutBalance = withoutBalance,
            isActive = isActive
        )
    }

}
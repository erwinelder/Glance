package com.ataglance.walletglance.account.domain.model

import com.ataglance.walletglance.account.domain.model.color.AccountColors

data class EditAccountUiState(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "",
    val currency: String = "",
    val balance: String = "0.00",
    val color: AccountColors = AccountColors.Default,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false
) {

    fun allowSaving(): Boolean {
        return name.isNotBlank() &&
                currency.isNotBlank() &&
                balance.isNotBlank() &&
                balance.last() != '.' &&
                balance.last() != '-'
    }

    fun toAccount(): Account? {
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

}
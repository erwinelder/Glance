package com.ataglance.walletglance.transfer.presentation.model

import com.ataglance.walletglance.account.domain.model.Account

data class TransferDraftItem(
    val account: Account? = null,
    val amount: String = "",
    val rate: String = "1"
) {

    fun savingIsAllowed(): Boolean {
        return amount.isNotBlank() && amount.lastOrNull() != '.' && amount.toDouble() != 0.0 &&
                account != null
    }

}

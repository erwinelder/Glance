package com.ataglance.walletglance.recordCreation.presentation.model.transfer

import com.ataglance.walletglance.account.domain.model.Account

data class TransferDraftUnits(
    val account: Account? = null,
    val recordNum: Int = 0,
    val recordId: Int = 0,
    val amount: String = "",
    val rate: String = "1"
) {

    fun savingIsAllowed(): Boolean {
        return amount.isNotBlank() && amount.lastOrNull() != '.' && amount.toDouble() != 0.0 &&
                account != null
    }

}

package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.account.domain.model.Account

data class TransferDraftSenderReceiver(
    val account: Account?,
    val recordNum: Int,
    val recordId: Int = 0,
    val amount: String = "",
    val rate: String = "1"
) {

    fun savingIsAllowed(): Boolean {
        return amount.isNotBlank() && amount.lastOrNull() != '.' && amount.toDouble() != 0.0 &&
                account != null
    }

}

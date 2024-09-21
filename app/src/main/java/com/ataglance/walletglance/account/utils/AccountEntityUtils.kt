package com.ataglance.walletglance.account.utils

import com.ataglance.walletglance.account.data.local.model.AccountEntity


fun List<AccountEntity>.findById(id: Int): AccountEntity? {
    return this.find { it.id == id }
}

fun List<AccountEntity>.checkOrderNumbers(): Boolean {
    this.sortedBy { it.orderNum }.forEachIndexed { index, account ->
        if (account.orderNum != index + 1) {
            return false
        }
    }
    return true
}

fun List<AccountEntity>.fixOrderNumbers(): List<AccountEntity> {
    return this.sortedBy { it.orderNum }.mapIndexed { index, account ->
        account.copy(orderNum = index + 1)
    }
}

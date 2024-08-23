package com.ataglance.walletglance.makingRecord.domain

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.record.data.local.model.RecordEntity

data class MadeTransferState(
    val recordIdFrom: Int,
    val recordIdTo: Int,
    val recordStatus: MakeRecordStatus,
    val fromAccount: Account,
    val toAccount: Account,
    val startAmount: Double,
    val finalAmount: Double,
    val dateTimeState: DateTimeState = DateTimeState(),
    val recordNum: Int,
    val includeInBudgets: Boolean = true
) {
    fun toRecordsPair(): Pair<RecordEntity, RecordEntity> {
        return Pair(
            RecordEntity(
                id = recordIdFrom,
                recordNum = recordNum,
                date = dateTimeState.dateLong,
                type = '>',
                amount = startAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = fromAccount.id,
                note = toAccount.id.toString(),
                includeInBudgets = includeInBudgets
            ),
            RecordEntity(
                id = recordIdTo,
                recordNum = recordNum + 1,
                date = dateTimeState.dateLong,
                type = '<',
                amount = finalAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = toAccount.id,
                note = fromAccount.id.toString(),
                includeInBudgets = includeInBudgets
            )
        )
    }
}
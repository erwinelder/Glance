package com.ataglance.walletglance.data.makingRecord

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.domain.entities.Record

data class MadeTransferState(
    val recordIdFrom: Int,
    val recordIdTo: Int,
    val recordStatus: MakeRecordStatus,
    val fromAccount: Account,
    val toAccount: Account,
    val startAmount: Double,
    val finalAmount: Double,
    val dateTimeState: DateTimeState = DateTimeState(),
    val recordNum: Int
) {
    fun toRecordsPair(): Pair<Record, Record> {
        return Pair(
            Record(
                id = recordIdFrom,
                recordNum = recordNum,
                date = dateTimeState.dateLong,
                type = '>',
                amount = startAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = fromAccount.id,
                note = toAccount.id.toString()
            ),
            Record(
                id = recordIdTo,
                recordNum = recordNum + 1,
                date = dateTimeState.dateLong,
                type = '<',
                amount = finalAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = toAccount.id,
                note = fromAccount.id.toString()
            )
        )
    }
}
package com.ataglance.walletglance.data.makingRecord

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.records.RecordType

data class MakeRecordUiState(
    val recordStatus: MakeRecordStatus,
    val recordNum: Int,
    val account: Account?,
    val type: RecordType = RecordType.Expense,
    val clickedUnitIndex: Int = 0,
    val dateTimeState: DateTimeState = DateTimeState(),
    val includeInBudgets: Boolean = true
)
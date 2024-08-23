package com.ataglance.walletglance.domain.makingRecord

import com.ataglance.walletglance.domain.accounts.Account
import com.ataglance.walletglance.domain.date.DateTimeState
import com.ataglance.walletglance.domain.records.RecordType

data class MakeRecordUiState(
    val recordStatus: MakeRecordStatus,
    val recordNum: Int,
    val account: Account?,
    val type: RecordType = RecordType.Expense,
    val clickedUnitIndex: Int = 0,
    val dateTimeState: DateTimeState = DateTimeState(),
    val includeInBudgets: Boolean = true
)
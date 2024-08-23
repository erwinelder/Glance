package com.ataglance.walletglance.makingRecord.domain

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.record.domain.RecordType

data class MakeRecordUiState(
    val recordStatus: MakeRecordStatus,
    val recordNum: Int,
    val account: Account?,
    val type: RecordType = RecordType.Expense,
    val clickedUnitIndex: Int = 0,
    val dateTimeState: DateTimeState = DateTimeState(),
    val includeInBudgets: Boolean = true
)
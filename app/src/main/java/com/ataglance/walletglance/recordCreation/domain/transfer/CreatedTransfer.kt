package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.core.domain.date.DateTimeState

data class CreatedTransfer(
    val isNew: Boolean,
    val sender: CreatedTransferUnit,
    val receiver: CreatedTransferUnit,
    val dateTimeState: DateTimeState,
    val includeInBudgets: Boolean
) {

    fun getUnitsRecordNums(): TransferUnitsRecordNums {
        return TransferUnitsRecordNums(sender = sender.recordNum, receiver = receiver.recordNum)
    }

}

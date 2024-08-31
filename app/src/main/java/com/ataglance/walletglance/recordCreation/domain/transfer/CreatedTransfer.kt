package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.core.domain.date.DateTimeState

data class CreatedTransfer(
    val isNew: Boolean,
    val sender: CreatedTransferSenderReceiver,
    val receiver: CreatedTransferSenderReceiver,
    val dateTimeState: DateTimeState,
    val includeInBudgets: Boolean
) {

    fun getSenderReceiverRecordNums(): TransferSenderReceiverRecordNums {
        return TransferSenderReceiverRecordNums(sender.recordNum, receiver.recordNum)
    }

}

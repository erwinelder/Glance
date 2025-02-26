package com.ataglance.walletglance.recordCreation.domain.transfer

data class CreatedTransfer(
    val isNew: Boolean,
    val sender: CreatedTransferUnit,
    val receiver: CreatedTransferUnit,
    val dateLong: Long,
    val includeInBudgets: Boolean
) {

    fun getUnitsRecordNums(): TransferUnitsRecordNums {
        return TransferUnitsRecordNums(sender = sender.recordNum, receiver = receiver.recordNum)
    }

}

package com.ataglance.walletglance.recordCreation.domain.mapper

import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.utils.asChar
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransferSenderReceiver
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraftSenderReceiver
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer


fun TransferDraftSenderReceiver.toCreatedTransferSenderReceiver(): CreatedTransferSenderReceiver? {
    this.account ?: return null
    val amount = this.amount.toDoubleOrNull() ?: return null
    val rate = this.rate.toDoubleOrNull() ?: return null

    return CreatedTransferSenderReceiver(
        account = this.account,
        recordNum = this.recordNum,
        recordId = this.recordId,
        amount = amount,
        rate = rate
    )
}

fun TransferDraft.toCreatedTransfer(): CreatedTransfer? {
    val sender = this.sender.toCreatedTransferSenderReceiver() ?: return null
    val receiver = this.receiver.toCreatedTransferSenderReceiver() ?: return null

    return CreatedTransfer(
        isNew = isNew,
        sender = sender,
        receiver = receiver,
        dateTimeState = dateTimeState,
        includeInBudgets = includeInBudgets
    )
}



fun CreatedTransfer.toRecordsPair(): Pair<RecordEntity, RecordEntity> {
    return Pair(
        RecordEntity(
            id = sender.recordId,
            recordNum = sender.recordNum,
            date = dateTimeState.dateLong,
            type = RecordType.OutTransfer.asChar(),
            amount = sender.amount,
            quantity = null,
            categoryId = 0,
            subcategoryId = null,
            accountId = sender.account.id,
            note = receiver.account.id.toString(),
            includeInBudgets = includeInBudgets
        ),
        RecordEntity(
            id = receiver.recordId,
            recordNum = receiver.recordNum,
            date = dateTimeState.dateLong,
            type = RecordType.InTransfer.asChar(),
            amount = receiver.amount,
            quantity = null,
            categoryId = 0,
            subcategoryId = null,
            accountId = receiver.account.id,
            note = sender.account.id.toString(),
            includeInBudgets = includeInBudgets
        )
    )
}

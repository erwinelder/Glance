package com.ataglance.walletglance.recordCreation.domain.mapper

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.utils.asChar
import com.ataglance.walletglance.record.utils.getStartAndFinalRateByAmounts
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransferSenderReceiver
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraftSenderReceiver
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer
import java.util.Locale


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

fun Pair<RecordStack, RecordStack>.toTransferDraft(
    accountList: List<Account>
): TransferDraft {
    val (startRate, finalRate) = getStartAndFinalRateByAmounts(
        this.first.totalAmount, this.second.totalAmount
    )
    val sender = TransferDraftSenderReceiver(
        account = accountList.findById(this.first.account.id),
        recordNum = this.first.recordNum,
        recordId = this.first.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.first.totalAmount),
        rate = "%.2f".format(Locale.US, startRate)
    )
    val receiver = TransferDraftSenderReceiver(
        account = accountList.findById(this.second.account.id),
        recordNum = this.second.recordNum,
        recordId = this.second.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.second.totalAmount),
        rate = "%.2f".format(Locale.US, finalRate)
    )
    return TransferDraft(
        isNew = false,
        sender = sender,
        receiver = receiver,
        dateTimeState = getNewDateByRecordLongDate(this.first.date),
        includeInBudgets = this.first.stack.firstOrNull()?.includeInBudgets ?: true,
        savingIsAllowed = sender.savingIsAllowed() && receiver.savingIsAllowed()
    )
}
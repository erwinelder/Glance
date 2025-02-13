package com.ataglance.walletglance.recordCreation.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.domain.utils.getStartAndFinalRateByAmounts
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransferUnit
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferPairRecordStacks
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraftUnits
import java.util.Locale


fun TransferPairRecordStacks.toTransferDraft(accounts: List<Account>): TransferDraft {
    val (startRate, finalRate) = getStartAndFinalRateByAmounts(
        this.sender.totalAmount, this.receiver.totalAmount
    )
    val sender = TransferDraftUnits(
        account = accounts.findById(this.sender.account.id),
        recordNum = this.sender.recordNum,
        recordId = this.sender.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.sender.totalAmount),
        rate = "%.2f".format(Locale.US, startRate)
    )
    val receiver = TransferDraftUnits(
        account = accounts.findById(this.receiver.account.id),
        recordNum = this.receiver.recordNum,
        recordId = this.receiver.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.receiver.totalAmount),
        rate = "%.2f".format(Locale.US, finalRate)
    )
    return TransferDraft(
        isNew = false,
        sender = sender,
        receiver = receiver,
        dateTimeState = getNewDateByRecordLongDate(this.sender.date),
        includeInBudgets = this.sender.stack.firstOrNull()?.includeInBudgets ?: true,
        savingIsAllowed = sender.savingIsAllowed() && receiver.savingIsAllowed()
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

fun TransferDraftUnits.toCreatedTransferSenderReceiver(): CreatedTransferUnit? {
    this.account ?: return null
    val amount = this.amount.toDoubleOrNull() ?: return null
    val rate = this.rate.toDoubleOrNull() ?: return null

    return CreatedTransferUnit(
        account = this.account,
        recordNum = this.recordNum,
        recordId = this.recordId,
        amount = amount,
        rate = rate
    )
}


fun CreatedTransfer.toRecordEntityPair(): Pair<RecordEntity, RecordEntity> {
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
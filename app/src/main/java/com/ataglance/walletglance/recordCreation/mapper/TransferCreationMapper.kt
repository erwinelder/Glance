package com.ataglance.walletglance.recordCreation.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.account.domain.utils.getOtherFrom
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.domain.utils.getOutAndInTransfersByRecordNum
import com.ataglance.walletglance.record.domain.utils.getStartAndFinalRateByAmounts
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransferUnit
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraftUnits
import java.util.Locale


fun List<RecordStack>.getTransferDraft(
    isNew: Boolean,
    recordNum: Int,
    accountsAndActiveOne: AccountsAndActiveOne
): TransferDraft {
    return this
        .takeUnless { isNew }
        ?.getOutAndInTransfersByRecordNum(recordNum)
        ?.toTransferDraft(accountList = accountsAndActiveOne.accountList)
        ?: getClearTransferDraft(recordNum = recordNum, accountsAndActiveOne = accountsAndActiveOne)
}

fun Pair<RecordStack, RecordStack>.toTransferDraft(accountList: List<Account>): TransferDraft {
    val (startRate, finalRate) = getStartAndFinalRateByAmounts(
        this.first.totalAmount, this.second.totalAmount
    )
    val sender = TransferDraftUnits(
        account = accountList.findById(this.first.account.id),
        recordNum = this.first.recordNum,
        recordId = this.first.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.first.totalAmount),
        rate = "%.2f".format(Locale.US, startRate)
    )
    val receiver = TransferDraftUnits(
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

private fun getClearTransferDraft(
    recordNum: Int,
    accountsAndActiveOne: AccountsAndActiveOne,
): TransferDraft {
    return TransferDraft(
        isNew = true,
        sender = TransferDraftUnits(
            account = accountsAndActiveOne.activeAccount,
            recordNum = recordNum
        ),
        receiver = TransferDraftUnits(
            account = accountsAndActiveOne.activeAccount?.let {
                accountsAndActiveOne.accountList.getOtherFrom(it)
            },
            recordNum = recordNum + 1
        )
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
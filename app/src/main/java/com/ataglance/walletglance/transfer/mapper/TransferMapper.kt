package com.ataglance.walletglance.transfer.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.roundToTwoDecimals
import com.ataglance.walletglance.core.utils.roundToTwoDecimalsAsString
import com.ataglance.walletglance.transfer.data.model.TransferDataModel
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft
import com.ataglance.walletglance.transfer.presentation.model.TransferDraftItem


fun Transfer.toDataModel(): TransferDataModel {
    return TransferDataModel(
        id = id,
        date = date,
        senderAccountId = sender.accountId,
        receiverAccountId = receiver.accountId,
        senderAmount = sender.amount,
        receiverAmount = receiver.amount,
        senderRate = sender.rate,
        receiverRate = receiver.rate,
        includeInBudgets = includeInBudgets
    )
}


fun TransferDataModel.toDomainModel(): Transfer {
    return Transfer(
        id = id,
        date = date,
        sender = TransferItem(
            accountId = senderAccountId, amount = senderAmount, rate = senderRate
        ),
        receiver = TransferItem(
            accountId = receiverAccountId, amount = receiverAmount, rate = receiverRate
        ),
        includeInBudgets = includeInBudgets
    )
}


fun TransferItem.toDraftItem(accounts: List<Account>): TransferDraftItem {
    val account = accounts.find { it.id == accountId }

    return TransferDraftItem(
        account = account,
        amount = amount.roundToTwoDecimalsAsString(),
        rate = rate.roundToTwoDecimalsAsString()
    )
}

fun Transfer.toDraft(accounts: List<Account>): TransferDraft {
    val sender = sender.toDraftItem(accounts = accounts)
    val receiver = receiver.toDraftItem(accounts = accounts)

    return TransferDraft.from(
        id = id,
        dateTimeState = DateTimeState.fromTimestamp(timestamp = date),
        sender = sender,
        receiver = receiver,
        includeInBudgets = includeInBudgets
    )
}


fun TransferDraftItem.toDomainModel(): TransferItem? {
    val accountId = account?.id ?: return null
    val amount = amount.toDoubleOrNull()?.roundToTwoDecimals() ?: return null
    val rate = rate.toDoubleOrNull()?.roundToTwoDecimals() ?: return null

    return TransferItem(accountId = accountId, amount = amount, rate = rate)
}

fun TransferDraft.toDomainModel(): Transfer? {
    val sender = sender.toDomainModel() ?: return null
    val receiver = receiver.toDomainModel() ?: return null

    return Transfer(
        id = id,
        date = dateTimeState.timestamp,
        sender = sender,
        receiver = receiver,
        includeInBudgets = includeInBudgets
    )
}

package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.domain.date.DateTimeState
import java.util.Locale

data class TransferDraft(
    val isNew: Boolean,
    val sender: TransferDraftSenderReceiver,
    val receiver: TransferDraftSenderReceiver,
    val dateTimeState: DateTimeState = DateTimeState(),
    val includeInBudgets: Boolean = true,
    val savingIsAllowed: Boolean = false
) {

    fun savingIsAllowed(
        sender: TransferDraftSenderReceiver = this.sender,
        receiver: TransferDraftSenderReceiver = this.receiver
    ): Boolean {
        return sender.savingIsAllowed() && receiver.savingIsAllowed() &&
                sender.account != receiver.account
    }

    fun getSenderReceiverRecordNums(): TransferSenderReceiverRecordNums {
        return TransferSenderReceiverRecordNums(sender.recordNum, receiver.recordId)
    }

    fun getAccount(isSender: Boolean): Account? {
        return if (isSender) sender.account else receiver.account
    }


    fun applyNewSenderAccount(account: Account): TransferDraft {
        return copy(sender = sender.copy(account = account))
    }

    fun applyNewReceiverAccount(account: Account): TransferDraft {
        return copy(receiver = receiver.copy(account = account))
    }


    fun applyRate(rate: String, isSender: Boolean): TransferDraft {
        return if (isSender) applySenderRate(rate) else applyReceiverRate(rate)
    }

    private fun applySenderRate(rate: String): TransferDraft {
        val newSender = sender.copy(rate = rate)
        val newReceiver = receiver.copy(amount = getAppropriateReceiverAmount(senderRate = rate))

        return copy(
            sender = newSender,
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(newSender, newReceiver)
        )
    }

    private fun applyReceiverRate(rate: String): TransferDraft {
        val newReceiver = receiver.copy(
            rate = rate,
            amount = getAppropriateReceiverAmount(receiverRate = rate)
        )

        return copy(
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(receiver = newReceiver)
        )
    }


    fun applyAmount(amount: String, isSender: Boolean): TransferDraft {
        return if (isSender) applySenderAmount(amount) else applyReceiverAmount(amount)
    }

    private fun applySenderAmount(amount: String): TransferDraft {
        val newSender = sender.copy(amount = amount)
        val newReceiver = receiver.copy(
            amount = getAppropriateReceiverAmount(senderAmount = amount)
        )

        return copy(
            sender = newSender,
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(newSender, newReceiver)
        )
    }

    private fun applyReceiverAmount(amount: String): TransferDraft {
        val newReceiver = receiver.copy(
            amount = amount,
            rate = getAppropriateReceiverRate(amount)
        )

        return copy(
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(receiver = newReceiver)
        )
    }


    private fun getAppropriateReceiverAmount(
        senderRate: String = sender.rate,
        receiverRate: String = receiver.rate,
        senderAmount: String = sender.amount
    ): String {
        return if (senderAmount.isBlank() || senderRate.isBlank() || receiverRate.isBlank()) {
            receiver.amount
        } else {
            "%.2f".format(
                Locale.US,
                senderAmount.toDouble() / senderRate.toDouble() * receiverRate.toDouble()
            )
        }
    }

    private fun getAppropriateReceiverRate(receiverAmount: String): String {
        return if (receiverAmount.isBlank() || sender.rate.isBlank() || sender.amount.isBlank()) {
            receiver.rate
        } else {
            "%.2f".format(
                Locale.US,
                receiverAmount.toDouble() * sender.rate.toDouble() / sender.amount.toDouble()
            )
        }
    }

}

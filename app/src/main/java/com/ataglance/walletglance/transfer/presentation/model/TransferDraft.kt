package com.ataglance.walletglance.transfer.presentation.model

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.roundToTwoDecimalsAsString

data class TransferDraft(
    val id: Long = 0,
    val dateTimeState: DateTimeState = DateTimeState.fromCurrentTime(),
    val sender: TransferDraftItem = TransferDraftItem(),
    val receiver: TransferDraftItem = TransferDraftItem(),
    val includeInBudgets: Boolean = true,
    val savingIsAllowed: Boolean = false
) {

    companion object {

        private fun companionSavingIsAllowed(
            sender: TransferDraftItem,
            receiver: TransferDraftItem
        ): Boolean {
            return sender.savingIsAllowed() && receiver.savingIsAllowed() &&
                    sender.account?.id != receiver.account?.id
        }

        fun from(
            id: Long,
            dateTimeState: DateTimeState,
            sender: TransferDraftItem,
            receiver: TransferDraftItem,
            includeInBudgets: Boolean
        ): TransferDraft {
            return TransferDraft(
                id = id,
                dateTimeState = dateTimeState,
                sender = sender,
                receiver = receiver,
                includeInBudgets = includeInBudgets,
                savingIsAllowed = companionSavingIsAllowed(sender = sender, receiver = receiver)
            )
        }

        fun asNew(
            senderAccount: Account? = null,
            receiverAccount: Account? = null
        ): TransferDraft {
            return TransferDraft(
                sender = TransferDraftItem(account = senderAccount),
                receiver = TransferDraftItem(account = receiverAccount)
            )
        }

    }


    val isNew: Boolean
        get() = id == 0L

    fun savingIsAllowed(
        sender: TransferDraftItem = this.sender,
        receiver: TransferDraftItem = this.receiver
    ): Boolean {
        return companionSavingIsAllowed(sender = sender, receiver = receiver)
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
        return if (isSender) applySenderRate(rate = rate) else applyReceiverRate(rate = rate)
    }

    private fun applySenderRate(rate: String): TransferDraft {
        val newSender = sender.copy(rate = rate)
        val newReceiver = receiver.copy(amount = getAppropriateReceiverAmount(senderRate = rate))

        return copy(
            sender = newSender,
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(sender = newSender, receiver = newReceiver)
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
        return if (isSender) applySenderAmount(amount = amount) else
            applyReceiverAmount(amount = amount)
    }

    private fun applySenderAmount(amount: String): TransferDraft {
        val newSender = sender.copy(amount = amount)
        val newReceiver = receiver.copy(
            amount = getAppropriateReceiverAmount(senderAmount = amount)
        )

        return copy(
            sender = newSender,
            receiver = newReceiver,
            savingIsAllowed = savingIsAllowed(sender = newSender, receiver = newReceiver)
        )
    }

    private fun applyReceiverAmount(amount: String): TransferDraft {
        val newReceiver = receiver.copy(
            amount = amount,
            rate = getAppropriateReceiverRate(receiverAmount = amount)
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
            (senderAmount.toDouble() / senderRate.toDouble() * receiverRate.toDouble())
                .roundToTwoDecimalsAsString()
        }
    }

    private fun getAppropriateReceiverRate(receiverAmount: String): String {
        return if (receiverAmount.isBlank() || sender.rate.isBlank() || sender.amount.isBlank()) {
            receiver.rate
        } else {
            (receiverAmount.toDouble() * sender.rate.toDouble() / sender.amount.toDouble())
                .roundToTwoDecimalsAsString()
        }
    }

}

package com.ataglance.walletglance.recordCreation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.getOtherFrom
import com.ataglance.walletglance.core.utils.isPositiveNumberWithDecimal
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferSenderReceiverRecordNums
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransferCreationViewModel(
    private val accountList: List<Account>,
    initialTransferDraft: TransferDraft
) : ViewModel() {

    private val _transferDraft: MutableStateFlow<TransferDraft> = MutableStateFlow(
        initialTransferDraft
    )
    val transferDraft: StateFlow<TransferDraft> = _transferDraft.asStateFlow()


    fun selectNewDate(selectedDateMillis: Long) {
        _transferDraft.update {
            it.copy(dateTimeState = it.dateTimeState.getNewDate(selectedDateMillis))
        }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _transferDraft.update {
            it.copy(dateTimeState = it.dateTimeState.getNewTime(hour, minute))
        }
    }


    fun selectAnotherAccount(isSender: Boolean) {
        if (accountList.size < 2) return
        val currentAccount = transferDraft.value.getAccount(isSender) ?: return

        val index = accountList.indexOfFirst { it.id == currentAccount.id }
        if (index == -1) return

        (accountList.getOrNull(index + 1) ?: accountList.getOrNull(index - 1))?.let { account ->
            selectAccount(account, isSender)
        }
    }

    fun selectAccount(account: Account, isSender: Boolean) {
        if (isSender) {
            selectSenderAccount(account)
        } else {
            selectReceiverAccount(account)
        }
    }

    private fun selectSenderAccount(account: Account) {
        if (transferDraft.value.receiver.account != account) {
            _transferDraft.update {
                it.applyNewSenderAccount(account)
            }
        } else {
            _transferDraft.update {
                it.copy(
                    sender = it.sender.copy(account = account),
                    receiver = it.receiver.copy(account = accountList.getOtherFrom(account))
                )
            }
        }
    }

    private fun selectReceiverAccount(account: Account) {
        if (transferDraft.value.sender.account != account) {
            _transferDraft.update {
                it.applyNewReceiverAccount(account)
            }
        } else {
            _transferDraft.update {
                it.copy(
                    sender = it.sender.copy(account = accountList.getOtherFrom(account)),
                    receiver = it.receiver.copy(account = account)
                )
            }
        }
    }


    fun changeRate(rate: String, isSender: Boolean) {
        val newRate = rate.takeIf { it.isPositiveNumberWithDecimal() } ?: return

        _transferDraft.update {
            it.applyRate(newRate, isSender)
        }
    }

    fun changeAmount(amount: String, isSender: Boolean) {
        val newAmount = amount.takeIf { it.isPositiveNumberWithDecimal() } ?: return

        _transferDraft.update {
            it.applyAmount(newAmount, isSender)
        }
    }

    fun getTransferDraft(): TransferDraft {
        return transferDraft.value
    }

    fun getSenderReceiverRecordNums(): TransferSenderReceiverRecordNums {
        return transferDraft.value.getSenderReceiverRecordNums()
    }

}

class TransferCreationViewModelFactory(
    private val accountList: List<Account>,
    private val transferDraft: TransferDraft
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransferCreationViewModel(accountList, transferDraft) as T
    }
}

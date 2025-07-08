package com.ataglance.walletglance.transfer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.getOtherFrom
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.isPositiveNumberWithDecimal
import com.ataglance.walletglance.transfer.domain.usecase.DeleteTransferUseCase
import com.ataglance.walletglance.transfer.domain.usecase.GetTransferDraftUseCase
import com.ataglance.walletglance.transfer.domain.usecase.SaveTransferUseCase
import com.ataglance.walletglance.transfer.mapper.toDomainModel
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferCreationViewModel(
    transferId: Long?,
    accountId: Int?,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val deleteTransferUseCase: DeleteTransferUseCase,
    private val getTransferDraftUseCase: GetTransferDraftUseCase,
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private var accounts: List<Account> = listOf()


    private val _transferDraft = MutableStateFlow(TransferDraft())
    val transferDraft = _transferDraft.asStateFlow()


    fun selectNewDate(timestamp: Long) {
        _transferDraft.update {
            it.copy(dateTimeState = it.dateTimeState.applyNewDate(timestamp = timestamp))
        }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _transferDraft.update {
            it.copy(dateTimeState = it.dateTimeState.applyNewTime(hour = hour, minute = minute))
        }
    }


    fun selectAnotherAccount(isSender: Boolean) {
        if (accounts.size < 2) return
        val currentAccount = transferDraft.value.getAccount(isSender) ?: return

        val index = accounts.indexOfFirst { it.id == currentAccount.id }
        if (index == -1) return

        (accounts.getOrNull(index + 1) ?: accounts.getOrNull(index - 1))?.let { account ->
            selectAccount(account = account, isSender = isSender)
        }
    }

    fun selectAccount(account: Account, isSender: Boolean) {
        if (isSender) {
            selectSenderAccount(account = account)
        } else {
            selectReceiverAccount(account = account)
        }
    }

    private fun selectSenderAccount(account: Account) {
        if (transferDraft.value.receiver.account != account) {
            _transferDraft.update {
                it.applyNewSenderAccount(account = account)
            }
        } else {
            _transferDraft.update {
                it.copy(
                    sender = it.sender.copy(account = account),
                    receiver = it.receiver.copy(account = accounts.getOtherFrom(account = account))
                )
            }
        }
    }

    private fun selectReceiverAccount(account: Account) {
        if (transferDraft.value.sender.account != account) {
            _transferDraft.update {
                it.applyNewReceiverAccount(account = account)
            }
        } else {
            _transferDraft.update {
                it.copy(
                    sender = it.sender.copy(account = accounts.getOtherFrom(account = account)),
                    receiver = it.receiver.copy(account = account)
                )
            }
        }
    }


    fun changeRate(rate: String, isSender: Boolean) {
        val newRate = rate.takeIf { it.isPositiveNumberWithDecimal() } ?: return

        _transferDraft.update {
            it.applyRate(rate = newRate, isSender = isSender)
        }
    }

    fun changeAmount(amount: String, isSender: Boolean) {
        val newAmount = amount.takeIf { it.isPositiveNumberWithDecimal() } ?: return

        _transferDraft.update {
            it.applyAmount(amount = newAmount, isSender = isSender)
        }
    }


    suspend fun saveTransfer() {
        val transfer = transferDraft.value.takeIf { it.savingIsAllowed }?.toDomainModel() ?: return

        saveTransferUseCase.execute(transfer = transfer)
    }

    suspend fun repeatTransfer() {
        val transfer = transferDraft.value
            .takeIf { it.savingIsAllowed }
            ?.copy(
                id = 0,
                dateTimeState = DateTimeState.fromCurrentTime()
            )
            ?.toDomainModel()
            ?: return

        saveTransferUseCase.execute(transfer = transfer)
    }

    suspend fun deleteTransfer() {
        val id = _transferDraft.value.id.takeUnless { it == 0L } ?: return

        deleteTransferUseCase.execute(transferId = id)
    }


    init {
        viewModelScope.launch {
            accounts = getAccountsUseCase.getAll()

            val transferDraft = getTransferDraftUseCase.execute(
                transferId = transferId,
                accountId = accountId,
                accounts = accounts
            )
            _transferDraft.update { transferDraft }
        }
    }

}
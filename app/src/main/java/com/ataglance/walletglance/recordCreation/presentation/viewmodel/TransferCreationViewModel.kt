package com.ataglance.walletglance.recordCreation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.getOtherFrom
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.isPositiveNumberWithDecimal
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteTransferUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.GetTransferDraftUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveTransferUseCase
import com.ataglance.walletglance.recordCreation.mapper.toCreatedTransfer
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferCreationViewModel(
    recordNum: Int?,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val deleteTransferUseCase: DeleteTransferUseCase,
    private val getTransferDraftUseCase: GetTransferDraftUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getLastRecordNumUseCase: GetLastRecordNumUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            accountList = getAccountsUseCase.getAll()

            val transferDraft = getTransferDraftUseCase.get(recordNum)
            _transferDraft.update { transferDraft }
        }
    }


    private var accountList: List<Account> = listOf()


    private val _transferDraft = MutableStateFlow(TransferDraft())
    val transferDraft = _transferDraft.asStateFlow()


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


    suspend fun saveTransfer() {
        transferDraft.value
            .takeIf { it.savingIsAllowed }
            ?.toCreatedTransfer()
            ?.let { createdTransfer ->
                saveTransferUseCase.execute(transfer = createdTransfer)
            }
    }

    suspend fun repeatTransfer() {
        val transferDraft = transferDraft.value.takeIf { it.savingIsAllowed } ?: return
        val recordNum = getLastRecordNumUseCase.getNext()

        val createdTransfer = transferDraft
            .copy(
                sender = transferDraft.sender.copy(
                    recordNum = recordNum,
                    recordId = 0
                ),
                receiver = transferDraft.receiver.copy(
                    recordNum = recordNum + 1,
                    recordId = 0
                ),
                dateTimeState = DateTimeState()
            )
            .toCreatedTransfer()
            ?: return

        saveTransferUseCase.execute(transfer = createdTransfer)
    }

    suspend fun deleteTransfer() {
        deleteTransferUseCase.execute(
            unitsRecordNums = transferDraft.value.getSenderReceiverRecordNums()
        )
    }

}
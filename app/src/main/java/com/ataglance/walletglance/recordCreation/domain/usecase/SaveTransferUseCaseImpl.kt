package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.account.domain.utils.mergeWith
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.record.data.model.DataAfterRecordOperation
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.usecase.GetTransferPairUseCase
import com.ataglance.walletglance.recordCreation.mapper.toRecordEntityPair
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer

class SaveTransferUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getTransferPairUseCase: GetTransferPairUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : SaveTransferUseCase {

    override suspend fun execute(transfer: CreatedTransfer) {
        val dataAfterRecordOperation = if (transfer.isNew) {
            getDataAfterNewTransfer(transfer)
        } else {
            getDataAfterEditedTransfer(transfer)
        } ?: return

        recordRepository.upsertRecords(records = dataAfterRecordOperation.recordsToUpsert)
        saveAccountsUseCase.upsert(accounts = dataAfterRecordOperation.accountsToUpsert)
    }

    private fun getDataAfterNewTransfer(transfer: CreatedTransfer): DataAfterRecordOperation {
        val updatedAccounts = listOf(
            transfer.sender.account.cloneAndSubtractFromBalance(transfer.sender.amount),
            transfer.receiver.account.cloneAndAddToBalance(transfer.receiver.amount)
        ).map(Account::toDataModel)

        return DataAfterRecordOperation(
            recordsToUpsert = transfer.toRecordEntityPair().toList(), accountsToUpsert = updatedAccounts
        )
    }

    private suspend fun getDataAfterEditedTransfer(
        transfer: CreatedTransfer
    ): DataAfterRecordOperation? {
        val transferUnits = getTransferPairUseCase
            .execute(unitsRecordNums = transfer.getUnitsRecordNums())
            ?: return null

        val records = transfer.toRecordEntityPair().toList()

        val updatedAccounts = getUpdatedAccountsAfterEditedTransfer(
            transfer = transfer,
            senderStack = transferUnits.sender,
            receiverStack = transferUnits.receiver
        )
            ?.map(Account::toDataModel)
            ?: return null

        return DataAfterRecordOperation(
            recordsToUpsert = records, accountsToUpsert = updatedAccounts
        )
    }

    private suspend fun getUpdatedAccountsAfterEditedTransfer(
        transfer: CreatedTransfer,
        senderStack: RecordStack,
        receiverStack: RecordStack
    ): List<Account>? {
        val prevFromAccount = getAccountsUseCase.get(id = senderStack.account.id) ?: return null
        val prevToAccount = getAccountsUseCase.get(id = receiverStack.account.id) ?: return null

        val updatedPreviousAccounts = listOf(
            prevFromAccount.cloneAndAddToBalance(senderStack.totalAmount),
            prevToAccount.cloneAndSubtractFromBalance(receiverStack.totalAmount)
        )
        val updatedAccounts = applyAmountsToAccountsAfterTransfer(
            transfer = transfer, prevAccounts = updatedPreviousAccounts
        )

        return updatedAccounts?.mergeWith(updatedPreviousAccounts)
    }

    private suspend fun applyAmountsToAccountsAfterTransfer(
        transfer: CreatedTransfer,
        prevAccounts: List<Account>
    ): List<Account>? {
        val updatedAccounts = mutableListOf<Account>()

        prevAccounts.findById(transfer.sender.account.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(transfer.sender.amount))
        } ?: getAccountsUseCase.get(id = transfer.sender.account.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(transfer.sender.amount))
        } ?: return null

        prevAccounts.findById(transfer.receiver.account.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(transfer.receiver.amount))
        } ?: getAccountsUseCase.get(transfer.receiver.account.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(transfer.receiver.amount))
        } ?: return null

        return updatedAccounts
    }

}
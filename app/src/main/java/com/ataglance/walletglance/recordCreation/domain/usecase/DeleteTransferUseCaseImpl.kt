package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.usecase.GetTransferPairUseCase
import com.ataglance.walletglance.record.mapper.toRecordEntities
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferUnitsRecordNums

class DeleteTransferUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getTransferPairUseCase: GetTransferPairUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : DeleteTransferUseCase {
    override suspend fun execute(unitsRecordNums: TransferUnitsRecordNums) {

        val transferUnits = getTransferPairUseCase.execute(unitsRecordNums = unitsRecordNums)
            ?: return
        val records = transferUnits.sender.toRecordEntities() + transferUnits.receiver.toRecordEntities()

        val senderAccount = getAccountsUseCase.get(id = transferUnits.sender.account.id)
            ?.cloneAndAddToBalance(transferUnits.sender.totalAmount)
            ?.toDataModel()
            ?: return
        val receiverAccount = getAccountsUseCase.get(id = transferUnits.receiver.account.id)
            ?.cloneAndSubtractFromBalance(transferUnits.receiver.totalAmount)
            ?.toDataModel()
            ?: return
        val updatedAccounts = listOf(senderAccount, receiverAccount)

        recordRepository.deleteRecords(records = records)
        saveAccountsUseCase.saveDataModels(accounts = updatedAccounts)
    }
}
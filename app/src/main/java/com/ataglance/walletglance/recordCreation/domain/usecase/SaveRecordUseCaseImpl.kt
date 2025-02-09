package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.category.domain.utils.toRecordType
import com.ataglance.walletglance.record.data.model.DataAfterRecordOperation
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase
import com.ataglance.walletglance.record.mapper.toRecordEntities
import com.ataglance.walletglance.recordCreation.mapper.toRecordEntities
import com.ataglance.walletglance.recordCreation.mapper.toRecordEntitiesWithOldIds
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecord

class SaveRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getRecordStackUseCase: GetRecordStackUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : SaveRecordUseCase {

    override suspend fun execute(record: CreatedRecord) {

        val dataAfterRecordOperation = if (record.isNew) {
            getDataAfterNewRecord(record)
        } else {
            getDataAfterEditedRecord(record)
        } ?: return

        recordRepository.deleteAndUpsertRecords(
            toDelete = dataAfterRecordOperation.recordsDelete,
            toUpsert = dataAfterRecordOperation.recordsToUpsert
        )
        saveAccountsUseCase.execute(accounts = dataAfterRecordOperation.accountsToUpsert)
    }

    private suspend fun getDataAfterNewRecord(record: CreatedRecord): DataAfterRecordOperation? {
        val records = record.toRecordEntities()

        val accounts = getAccountsUseCase.get(id = record.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = record.totalAmount,
                recordType = record.type.toRecordType()
            )
            ?.toDataModel()
            ?.let { listOf(it) }
            ?: return null

        return DataAfterRecordOperation(recordsToUpsert = records, accountsToUpsert = accounts)
    }

    private suspend fun getDataAfterEditedRecord(record: CreatedRecord): DataAfterRecordOperation? {
        val currRecordStack = getRecordStackUseCase.get(recordNum = record.recordNum) ?: return null
        val updatedAccounts = getUpdatedAccountsAfterEditedRecord(
            record = record, currRecordStack = currRecordStack
        )
            ?.map(Account::toDataModel)
            ?: return null

        return if (record.items.size == currRecordStack.stack.size) {
            DataAfterRecordOperation(
                recordsToUpsert = record.toRecordEntitiesWithOldIds(currRecordStack),
                accountsToUpsert = updatedAccounts,
            )
        } else {
            DataAfterRecordOperation(
                recordsDelete = currRecordStack.toRecordEntities(),
                recordsToUpsert = record.toRecordEntities(),
                accountsToUpsert = updatedAccounts,
            )
        }
    }

    private suspend fun getUpdatedAccountsAfterEditedRecord(
        record: CreatedRecord,
        currRecordStack: RecordStack
    ): List<Account>? {
        return if (record.account.id == currRecordStack.account.id) {
            listOf(
                record.account.cloneAndReapplyAmountToBalance(
                    prevAmount = currRecordStack.totalAmount,
                    newAmount = record.totalAmount,
                    recordType = record.type.toRecordType()
                )
            )
        } else {
            val prevAccount = getAccountsUseCase.get(id = currRecordStack.account.id) ?: return null
            Pair(prevAccount, record.account)
                .returnAmountToFirstBalanceAndUpdateSecondBalance(
                    prevAmount = currRecordStack.totalAmount,
                    newAmount = record.totalAmount,
                    recordType = record.type.toRecordType()
                )
                .toList()
        }
    }

}
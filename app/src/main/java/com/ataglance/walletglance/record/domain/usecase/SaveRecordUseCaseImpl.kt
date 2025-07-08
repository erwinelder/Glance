package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.mapper.toDataModelWithItems
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class SaveRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getRecordUseCase: GetRecordUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : SaveRecordUseCase {

    override suspend fun execute(createdRecord: RecordWithItems) {
        if (createdRecord.isNew) {
            val accounts = getAccountsAfterNewRecord(createdRecord = createdRecord) ?: return

            saveAccountsUseCase.save(accounts = accounts.map { it.toDataModel() })
            recordRepository.upsertRecordWithItems(
                recordWithItems = createdRecord.toDataModelWithItems()
            )
        } else {
            val currentRecord = getRecordUseCase.get(id = createdRecord.recordId) ?: return
            val accounts = getAccountsAfterEditedRecord(
                createdRecord = createdRecord, currentRecord = currentRecord
            ) ?: return

            saveAccountsUseCase.save(accounts = accounts.map { it.toDataModel() })
            recordRepository.deleteAndUpsertRecordWithItems(
                recordWithItemsToDelete = currentRecord.toDataModelWithItems(),
                recordWithItemsToUpsert = createdRecord.toDataModelWithItems()
            )
        }
    }

    private suspend fun getAccountsAfterNewRecord(
        createdRecord: RecordWithItems
    ): List<Account>? {
        return getAccountsUseCase
            .get(id = createdRecord.accountId)
            ?.applyTransaction(
                amount = createdRecord.totalAmount, type = createdRecord.type
            )
            ?.asList()
    }

    private suspend fun getAccountsAfterEditedRecord(
        createdRecord: RecordWithItems,
        currentRecord: RecordWithItems
    ): List<Account>? {
        return if (createdRecord.accountId == currentRecord.accountId) {
            getAccountsUseCase
                .get(id = createdRecord.accountId)
                ?.reapplyTransaction(
                    prevAmount = currentRecord.totalAmount,
                    newAmount = createdRecord.totalAmount,
                    type = createdRecord.type
                )
                ?.asList()
        } else {
            val prevAccount = getAccountsUseCase
                .get(id = currentRecord.accountId)
                ?.rollbackTransaction(amount = currentRecord.totalAmount, type = currentRecord.type)
                ?: return null
            val newAccount = getAccountsUseCase
                .get(id = createdRecord.accountId)
                ?.applyTransaction(amount = createdRecord.totalAmount, type = createdRecord.type)
                ?: return null
            listOf(prevAccount, newAccount)
        }
    }

}
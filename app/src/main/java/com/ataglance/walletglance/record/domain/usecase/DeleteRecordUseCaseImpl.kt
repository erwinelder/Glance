package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.record.data.repository.RecordRepository

class DeleteRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : DeleteRecordUseCase {
    
    override suspend fun execute(id: Long) {
        val record = recordRepository.getRecordWithItems(id = id) ?: return
        val type = CategoryType.fromChar(char = record.type) ?: return

        val account = getAccountsUseCase.get(id = record.accountId)
            ?.rollbackTransaction(amount = record.totalAmount, type = type)
            ?: return

        recordRepository.deleteRecordWithItems(recordWithItems = record)
        saveAccountsUseCase.save(account = account.toDataModel())
    }

}
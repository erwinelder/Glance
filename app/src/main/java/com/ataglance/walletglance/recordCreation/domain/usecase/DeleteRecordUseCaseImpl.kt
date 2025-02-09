package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase
import com.ataglance.walletglance.record.domain.utils.inverse
import com.ataglance.walletglance.record.mapper.toRecordEntities

class DeleteRecordUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getRecordStackUseCase: GetRecordStackUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : DeleteRecordUseCase {
    override suspend fun execute(recordNum: Int) {
        val recordStack = getRecordStackUseCase.get(recordNum = recordNum) ?: return

        val updatedAccounts = getAccountsUseCase.get(id = recordStack.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = recordStack.totalAmount, recordType = recordStack.type.inverse()
            )
            ?.toDataModel()
            ?.let { listOf(it) }
            ?: return

        recordRepository.deleteRecords(records = recordStack.toRecordEntities())
        saveAccountsUseCase.execute(accounts = updatedAccounts)
    }
}
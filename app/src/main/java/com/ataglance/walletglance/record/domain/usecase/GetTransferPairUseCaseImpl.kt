package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferPairRecordStacks
import com.ataglance.walletglance.record.mapper.toRecordStack
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferUnitsRecordNums

class GetTransferPairUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : GetTransferPairUseCase {
    override suspend fun execute(unitsRecordNums: TransferUnitsRecordNums): TransferPairRecordStacks? {
        val accounts = getAccountsUseCase.getAll()
        val categories = getCategoriesUseCase.getGrouped()

        val outRecordStack = recordRepository
            .getRecordsByRecordNum(recordNum = unitsRecordNums.sender)
            .toRecordStack(accounts = accounts, groupedCategoriesByType = categories)
            ?: return null
        val inRecordStack = recordRepository
            .getRecordsByRecordNum(recordNum = unitsRecordNums.receiver)
            .toRecordStack(accounts = accounts, groupedCategoriesByType = categories)
            ?: return null

        return TransferPairRecordStacks(sender = outRecordStack, receiver = inRecordStack)
    }
}
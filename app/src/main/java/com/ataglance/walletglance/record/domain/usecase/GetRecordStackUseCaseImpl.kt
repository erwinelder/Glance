package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCase
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStack

class GetRecordStackUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : GetRecordStackUseCase {
    override suspend fun get(recordNum: Int): RecordStack? {
        val records = recordRepository.getRecordsByRecordNum(recordNum = recordNum)
        val accounts = getAccountsUseCase.getAll()
        val categories = getAllCategoriesUseCase.get()

        return records.toRecordStack(
            accounts = accounts, categoriesWithSubcategories = categories
        )
    }
}
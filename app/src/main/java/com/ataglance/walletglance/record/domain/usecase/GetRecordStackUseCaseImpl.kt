package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStack

class GetRecordStackUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : GetRecordStackUseCase {

    override suspend fun get(recordNum: Int): RecordStack? {
        val records = recordRepository.getRecordsByRecordNum(recordNum = recordNum)
        val accounts = getAccountsUseCase.getAll()
        val categories = getCategoriesUseCase.getGrouped()

        return records.toRecordStack(accounts = accounts, groupedCategoriesByType = categories)
    }

    override suspend fun getLastByTypeAndAccount(
        type: CategoryType,
        accountId: Int,
        categories: GroupedCategoriesByType
    ): RecordStack? {
        val accounts = getAccountsUseCase.getAll()

        return recordRepository
            .getLastRecordsByTypeAndAccount(type = type.asChar(), accountId = accountId)
            .toRecordStack(accounts = accounts, groupedCategoriesByType = categories)
    }

}
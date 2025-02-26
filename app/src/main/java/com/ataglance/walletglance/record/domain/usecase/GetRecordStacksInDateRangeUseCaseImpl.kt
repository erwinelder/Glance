package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStacks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetRecordStacksInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : GetRecordStacksInDateRangeUseCase {

    override fun getFlow(range: LongDateRange): Flow<List<RecordStack>> = combine(
        recordRepository.getRecordsInDateRangeFlow(range = range),
        getAccountsUseCase.getAllFlow(),
        getCategoriesUseCase.getGroupedFlow()
    ) { records, accounts, categories ->
        records.toRecordStacks(accounts = accounts, groupedCategoriesByType = categories)
    }

    override suspend fun get(range: LongDateRange): List<RecordStack> {
        val records = recordRepository.getRecordsInDateRange(range = range)
        val accounts = getAccountsUseCase.getAll()
        val categories = getCategoriesUseCase.getGrouped()

        return records.toRecordStacks(accounts = accounts, groupedCategoriesByType = categories)
    }
}
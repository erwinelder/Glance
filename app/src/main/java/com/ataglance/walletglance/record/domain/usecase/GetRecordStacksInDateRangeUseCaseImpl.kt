package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStacks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetRecordStacksInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : GetRecordStacksInDateRangeUseCase {

    override fun getAsFlow(range: LongDateRange): Flow<List<RecordStack>> = flow {
        val recordsFlow = recordRepository.getRecordsInDateRange(range = range)
        val accountsFlow = getAccountsUseCase.getAllAsFlow()
        val categoriesFlow = getCategoriesUseCase.getGroupedAsFlow()

        combine(recordsFlow, accountsFlow, categoriesFlow) { records, accounts, categories ->
            records.toRecordStacks(accounts = accounts, categoriesWithSubcategories = categories)
        }.collect(::emit)
    }

    override suspend fun get(range: LongDateRange): List<RecordStack> {
        val records = recordRepository.getRecordsInDateRange(range = range).firstOrNull().orEmpty()
        val accounts = getAccountsUseCase.getAll()
        val categories = getCategoriesUseCase.getGrouped()

        return records.toRecordStacks(accounts = accounts, categoriesWithSubcategories = categories)
    }
}
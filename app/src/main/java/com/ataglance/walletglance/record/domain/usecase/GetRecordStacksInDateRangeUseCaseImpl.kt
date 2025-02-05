package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAllAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCase
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStackList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetRecordStacksInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : GetRecordStacksInDateRangeUseCase {

    override fun getAsFlow(range: LongDateRange): Flow<List<RecordStack>> = flow {
        val recordsFlow = recordRepository.getRecordsInDateRange(range = range)
        val accountsFlow = getAllAccountsUseCase.getAsFlow()
        val categoriesFlow = getAllCategoriesUseCase.getAsFlow()

        combine(recordsFlow, accountsFlow, categoriesFlow) { records, accounts, categories ->
            records.toRecordStackList(accounts, categories)
        }.collect(::emit)
    }

    override suspend fun get(range: LongDateRange): List<RecordStack> {
        val records = recordRepository.getRecordsInDateRange(range = range).firstOrNull().orEmpty()
        val accounts = getAllAccountsUseCase.get()
        val categories = getAllCategoriesUseCase.get()

        return records.toRecordStackList(accounts, categories)
    }
}
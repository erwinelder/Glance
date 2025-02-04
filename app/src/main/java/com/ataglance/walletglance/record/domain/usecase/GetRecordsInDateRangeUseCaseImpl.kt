package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAllAccountsUseCase
import com.ataglance.walletglance.category.domain.usecase.GetAllCategoriesUseCase
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.mapper.toRecordStackList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : GetRecordsInDateRangeUseCase {
    override fun getAsFlow(range: LongDateRange): Flow<List<RecordStack>> = flow {
        val recordsFlow = recordRepository.getRecordsInDateRange(range = range)
        val accountsFlow = getAllAccountsUseCase.getAsFlow()
        val categoriesFlow = getAllCategoriesUseCase.getAsFlow()

        combine(recordsFlow, accountsFlow, categoriesFlow) { records, accounts, categories ->
            records.toRecordStackList(accounts, categories)
        }.collect(::emit)
    }
}
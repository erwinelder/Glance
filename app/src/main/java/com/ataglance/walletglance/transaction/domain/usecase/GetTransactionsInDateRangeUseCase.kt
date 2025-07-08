package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface GetTransactionsInDateRangeUseCase {

    fun getAsFlowOrEmpty(range: TimestampRange?): Flow<List<Transaction>> {
        return range?.let { getAsFlow(range = it) } ?: flowOf(emptyList())
    }

    fun getAsFlow(range: TimestampRange): Flow<List<Transaction>>

    suspend fun get(range: TimestampRange): List<Transaction>

}
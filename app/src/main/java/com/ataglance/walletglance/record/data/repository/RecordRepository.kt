package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.data.repository.BaseEntityRepository
import com.ataglance.walletglance.record.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordRepository : BaseEntityRepository<RecordEntity> {

    suspend fun convertRecordsToTransfers(
        noteValues: List<String>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

    fun getLastRecordNum(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<Int?>

    fun getRecordsForToday(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<RecordEntity>>

    fun getRecordsInDateRange(
        longDateRange: LongDateRange,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<RecordEntity>>

    fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<TotalAmountByRange>>

}
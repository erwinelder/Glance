package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import com.ataglance.walletglance.core.utils.getTodayLongDateRange
import com.ataglance.walletglance.record.data.local.RecordLocalDataSource
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.data.remote.RecordRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class RecordRepositoryImpl(
    override val localSource: RecordLocalDataSource,
    override val remoteSource: RecordRemoteDataSource?
) : RecordRepository {

    override suspend fun convertRecordsToTransfers(
        noteValues: List<String>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.convertTransfersToRecords(noteValues, timestamp)
        remoteSource?.convertTransfersToRecords(
            noteValues = noteValues,
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllRecords(timestamp)
        remoteSource?.deleteAllEntities(timestamp, onSuccessListener, onFailureListener)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllRecords(timestamp = timestamp)
    }

    override fun getLastRecordNum(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<Int?> = syncDataAndGetFlowWrapper(
        flowSource = localSource::getLastRecordOrderNum,
        onSuccessListener = onSuccessListener,
        onFailureListener = onFailureListener
    )

    override fun getRecordsForToday(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<RecordEntity>> = syncDataAndGetFlowWrapper(
        flowSource = { localSource.getRecordsInDateRange(getTodayLongDateRange()) },
        onSuccessListener = onSuccessListener,
        onFailureListener = onFailureListener
    )

    override fun getRecordsInDateRange(
        longDateRange: LongDateRange,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<RecordEntity>> = syncDataAndGetFlowWrapper(
        flowSource = { localSource.getRecordsInDateRange(longDateRange) },
        onSuccessListener = onSuccessListener,
        onFailureListener = onFailureListener
    )

    override fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<TotalAmountByRange>> = syncAndExecute(
        onExecute = {
            val initialList = dateRangeList.map { TotalAmountByRange(it, 0.0) }
            emit(initialList)

            budget.category ?: return@syncAndExecute

            val budgetsAmountsByRanges = initialList.toMutableList()

            dateRangeList.forEachIndexed { index, dateRange ->
                val totalAmount = localSource.getTotalAmountForBudgetInDateRange(
                    linkedAccountsIds = budget.linkedAccountsIds,
                    categoryId = budget.category.id,
                    longDateRange = dateRange
                ).firstOrNull() ?: 0.0

                budgetsAmountsByRanges[index] = TotalAmountByRange(dateRange, totalAmount)
                emit(budgetsAmountsByRanges)
            }
        },
        onSuccessListener = onSuccessListener,
        onFailureListener = onFailureListener
    )

}
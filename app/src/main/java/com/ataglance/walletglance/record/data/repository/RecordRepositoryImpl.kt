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

    override suspend fun convertRecordsToTransfers(noteValues: List<String>) {
        val timestamp = getNowDateTimeLong()
        localSource.convertTransfersToRecords(noteValues, timestamp)
        remoteSource?.convertTransfersToRecords(noteValues = noteValues, timestamp = timestamp)
    }

    override suspend fun deleteAllEntities() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllRecords(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllRecords(timestamp = timestamp)
    }

    override fun getLastRecordNum(): Flow<Int?> = syncDataAndGetFlowWrapper {
        localSource.getLastRecordOrderNum()
    }

    override fun getRecordsForToday(): Flow<List<RecordEntity>> = syncDataAndGetFlowWrapper {
        localSource.getRecordsInDateRange(getTodayLongDateRange())
    }

    override fun getRecordsInDateRange(
        longDateRange: LongDateRange
    ): Flow<List<RecordEntity>> = syncDataAndGetFlowWrapper {
        localSource.getRecordsInDateRange(longDateRange)
    }

    override fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountByRange>> = syncAndExecute {
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
    }

}
package com.ataglance.walletglance.data.repository

import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.TotalAmountByRange
import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.domain.utils.getTodayLongDateRange
import com.ataglance.walletglance.data.local.dao.RecordDao
import com.ataglance.walletglance.data.local.entities.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RecordRepository(
    private val dao: RecordDao
) {

    fun getLastRecordNum(): Flow<Int?> {
        return dao.getLastRecordOrderNum()
    }

    fun getAllRecords(): Flow<List<Record>> {
        return dao.getAllRecords()
    }

    fun getRecordsForToday(): Flow<List<Record>> {
        val todayDateRange = getTodayLongDateRange()
        return dao.getRecordsInDateRange(todayDateRange.from, todayDateRange.to)
    }

    fun getRecordsInDateRange(longDateRange: LongDateRange): Flow<List<Record>> {
        return dao.getRecordsInDateRange(longDateRange.from, longDateRange.to)
    }

    fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountByRange>> = flow {
        val initialList = dateRangeList.map { TotalAmountByRange(it, 0.0) }
        emit(initialList)

        budget.category ?: return@flow

        val budgetsAmountsByRanges = initialList.toMutableList()

        dateRangeList.forEachIndexed { index, dateRange ->
            val totalAmount = dao.getTotalAmountForBudgetInDateRange(
                linkedAccountsIds = budget.linkedAccountsIds,
                categoryId = budget.category.id,
                from = dateRange.from,
                to = dateRange.to
            ).firstOrNull() ?: 0.0

            budgetsAmountsByRanges[index] = TotalAmountByRange(dateRange, totalAmount)
            emit(budgetsAmountsByRanges)
        }
    }

}
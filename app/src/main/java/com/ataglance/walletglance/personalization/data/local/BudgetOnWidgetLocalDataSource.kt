package com.ataglance.walletglance.personalization.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetOnWidgetLocalDataSource(
    private val budgetOnWidgetDao: BudgetOnWidgetDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<BudgetOnWidgetEntity>(
    dao = budgetOnWidgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.BudgetOnWidget
) {

    @Transaction
    suspend fun deleteBudgetsThatAreNotInList(budgetIds: List<Int>, timestamp: Long) {
        budgetOnWidgetDao.deleteBudgetsThatAreNotInList(budgetIds)
        updateLastModifiedTime(timestamp)
    }

    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>> =
        budgetOnWidgetDao.getAllBudgetsOnWidget()

}
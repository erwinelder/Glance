package com.ataglance.walletglance.personalization.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity

class BudgetOnWidgetLocalDataSource(
    private val budgetOnWidgetDao: BudgetOnWidgetDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<BudgetOnWidgetEntity>(
    dao = budgetOnWidgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.BudgetOnWidget
) {

    @Transaction
    suspend fun deleteAllBudgetsOnWidget(timestamp: Long) {
        budgetOnWidgetDao.deleteAllBudgetsOnWidget()
        updateLastModifiedTime(timestamp)
    }

}
package com.ataglance.walletglance.core.data.local

import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.first

abstract class BaseLocalDataSource(
    private val updateTimeDao: TableUpdateTimeDao,
    private val tableName: TableName
) {

    suspend fun updateTime(timestamp: Long) {
        updateTimeDao.updateTime(tableName.name, timestamp)
    }

    suspend fun getUpdateTime(): Long {
        return updateTimeDao.getUpdateTime(tableName.name).first()
    }

}
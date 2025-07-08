package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.core.utils.getCurrentTimestamp

object TablesSyncContext {

    private val tables = mutableMapOf<TableName, Long>()

    fun setTableUpdated(tableName: TableName) {
        tables[tableName] = getCurrentTimestamp()
    }

    fun tableIsUpdated(tableName: TableName): Boolean {
        val tableTimestamp = tables[tableName] ?: return false
        val currentTimestamp = getCurrentTimestamp()
        return currentTimestamp - tableTimestamp < 60_000
    }

    fun tableNotUpdated(tableName: TableName): Boolean {
        return !tableIsUpdated(tableName)
    }

}

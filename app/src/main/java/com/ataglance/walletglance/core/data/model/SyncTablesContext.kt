package com.ataglance.walletglance.core.data.model

class SyncTablesContext {

    private val tables = mutableMapOf<TableName, Boolean>()

    fun setTableUpdated(tableName: TableName) {
        tables[tableName] = true
    }

    fun tableIsUpdated(tableName: TableName): Boolean {
        return tables[tableName] ?: false
    }

    fun tableNotUpdated(tableName: TableName): Boolean {
        return !tableIsUpdated(tableName)
    }

}

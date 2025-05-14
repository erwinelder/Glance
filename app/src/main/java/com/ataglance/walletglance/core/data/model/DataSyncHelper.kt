package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.auth.domain.model.UserContext

class DataSyncHelper(
    private val syncTablesContext: SyncTablesContext,
    private val userContext: UserContext
) {

    fun getUserIdForSync(tableName: TableName): String? {
        return if (syncTablesContext.tableNotUpdated(tableName)) {
            userContext.getUserIdIfEligibleForDataSync()
        } else null
    }

    fun getUserIdForSync(vararg tableName: TableName): String? {
        return if (tableName.find { syncTablesContext.tableNotUpdated(it) } != null) {
            userContext.getUserIdIfEligibleForDataSync()
        } else null
    }

    suspend fun tryToSyncToRemote(
        tableName: TableName,
        synchroniser: suspend (userId: String) -> Unit
    ) {
        userContext.getUserIdIfEligibleForDataSync()?.let { userId ->
            synchroniser(userId)
            syncTablesContext.setTableUpdated(tableName)
        }
    }

    suspend fun tryToSyncToRemote(
        vararg tableName: TableName,
        synchroniser: suspend (userId: String) -> Unit
    ) {
        userContext.getUserIdIfEligibleForDataSync()?.let { userId ->
            synchroniser(userId)
            tableName.forEach { syncTablesContext.setTableUpdated(it) }
        }
    }

}
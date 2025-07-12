package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.utils.getCurrentTimestamp

class DataSyncHelper(
    private val tablesSyncContext: TablesSyncContext = TablesSyncContext,
    private val userContext: UserContext
) {

    fun getUserIdForSync(): Int? {
        return userContext.getUserIdIfEligibleForDataSync()
    }

    fun getUserIdForSync(tableName: TableName): Int? {
        return if (tablesSyncContext.tableNotUpdated(tableName)) {
            userContext.getUserIdIfEligibleForDataSync()
        } else null
    }

    fun setTableSynced(tableName: TableName) {
        tablesSyncContext.setTableUpdated(tableName = tableName)
    }


    suspend fun <E, QD, CD> synchronizeData(
        tableName: TableName,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (userId: Int) -> Long?,
        localDataGetter: suspend (timestamp: Long) -> List<E>,
        remoteDataGetter: suspend (timestamp: Long, userId: Int) -> List<QD>?,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        remoteSynchronizer: suspend (List<CD>, timestamp: Long, userId: Int) -> Unit,
        entityDeletedPredicate: (E) -> Boolean,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val userId = getUserIdForSync(tableName = tableName) ?: return
        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userId) ?: 0

        // No synchronization needed
        if (localTimestamp == remoteTimestamp) return setTableSynced(tableName = tableName)

        if (localTimestamp > remoteTimestamp) {
            // Synchronize from local to remote
            val dataToSync = localDataGetter(remoteTimestamp).map(entityToCommandDtoMapper)
            remoteSynchronizer(dataToSync, localTimestamp, userId)
        } else {
            // Synchronize from remote to local
            val (entitiesToDelete, entitiesToUpsert) = remoteDataGetter(localTimestamp, userId)
                ?.map(queryDtoToEntityMapper)
                ?.partition(entityDeletedPredicate)
                ?: return
            localHardCommand(entitiesToDelete, entitiesToUpsert, remoteTimestamp)
        }
    }

    suspend fun <DM, E, QD, CD> upsertData(
        data: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (userId: Int) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> List<E>,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, userId: Int) -> Unit,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            userId: Int,
            localTimestamp: Long
        ) -> List<QD>?,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        dataModelToCommandDtoMapper: (DM, timestamp: Long, deleted: Boolean) -> CD,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userId = getUserIdForSync()

        // Data cannot be synchronized with remote, save locally
        if (userId == null) {
            val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
            localSoftCommand(entities, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userId) ?: 0

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entities, timestamp)
                val dtos = localDataAfterTimestampGetter(remoteTimestamp).map(entityToCommandDtoMapper)
                remoteSoftCommand(dtos, timestamp, userId)
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val dtos = data.map { dataModelToCommandDtoMapper(it, timestamp, false) }
                val entities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, userId, localTimestamp
                )?.map(queryDtoToEntityMapper)
                    ?: data.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entities, timestamp)
            }
            // Data is up to date, save locally and remotely
            else -> {
                val entities = data.map { dataModelToEntityMapper(it, timestamp, false) }
                val dtos = localSoftCommand(entities, timestamp).map(entityToCommandDtoMapper)
                remoteSoftCommand(dtos, timestamp, userId)
            }
        }
    }

    suspend fun <DM, E, QD, CD> deleteData(
        data: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (userId: Int) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> Unit,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        localDeleteCommand: suspend (entities: List<E>, timestamp: Long?) -> Unit,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, userId: Int) -> Boolean,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            userId: Int,
            localTimestamp: Long
        ) -> List<QD>?,
        entityDeletedPredicate: (E) -> Boolean,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        dataModelToCommandDtoMapper: (DM, timestamp: Long, deleted: Boolean) -> CD,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userId = getUserIdForSync()

        // Data cannot be synchronized with remote, hard save locally
        if (userId == null) {
            val entities = data.map { dataModelToEntityMapper(it, timestamp, true) }
            localDeleteCommand(entities, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userId) ?: 0

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entitiesToDelete = data.map { dataModelToEntityMapper(it, timestamp, true) }
                localSoftCommand(entitiesToDelete, timestamp)

                val entities = localDataAfterTimestampGetter(remoteTimestamp)
                val dtos = entities.map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userId)

                if (result) {
                    localDeleteCommand(entitiesToDelete, null)
                }
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val dtos = data.map { dataModelToCommandDtoMapper(it, timestamp, true) }
                val entities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, userId, localTimestamp
                )?.map(queryDtoToEntityMapper)
                if (entities != null) {
                    val (entitiesToDelete, entitiesToUpsert) = entities.partition(entityDeletedPredicate)
                    localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
                } else {
                    val entities = data.map { dataModelToEntityMapper(it, timestamp, true) }
                    localSoftCommand(entities, timestamp)
                }
            }
            // Data is up to date, save locally and remotely
            else -> {
                val dtos = data.map { dataModelToCommandDtoMapper(it, timestamp, true) }
                val result = remoteSoftCommand(dtos, timestamp, userId)

                val entitiesToDelete = data.map { dataModelToEntityMapper(it, timestamp, true) }
                if (result) {
                    localDeleteCommand(entitiesToDelete, timestamp)
                } else {
                    localSoftCommand(entitiesToDelete, timestamp)
                }
            }
        }
    }

    suspend fun <DM, E, QD, CD> deleteAndUpsertData(
        toDelete: List<DM>,
        toUpsert: List<DM>,
        localTimestampGetter: suspend () -> Long?,
        remoteTimestampGetter: suspend (userId: Int) -> Long?,
        localSoftCommand: suspend (entities: List<E>, timestamp: Long) -> List<E>,
        localHardCommand: suspend (toDelete: List<E>, toUpsert: List<E>, timestamp: Long) -> Unit,
        localDeleteCommand: suspend (entities: List<E>) -> Unit,
        remoteSoftCommand: suspend (dtos: List<CD>, timestamp: Long, userId: Int) -> Boolean,
        localDataAfterTimestampGetter: suspend (timestamp: Long) -> List<E>,
        remoteSoftCommandAndDataAfterTimestampGetter: suspend (
            dtos: List<CD>,
            timestamp: Long,
            userId: Int,
            localTimestamp: Long
        ) -> List<QD>?,
        entityDeletedPredicate: (E) -> Boolean,
        dataModelToEntityMapper: (DM, timestamp: Long, deleted: Boolean) -> E,
        dataModelToCommandDtoMapper: (DM, timestamp: Long, deleted: Boolean) -> CD,
        entityToCommandDtoMapper: (E) -> CD,
        queryDtoToEntityMapper: (QD) -> E
    ) {
        val timestamp = getCurrentTimestamp()
        val userId = getUserIdForSync()

        // Data cannot be synchronized with remote, hard save locally
        if (userId == null) {
            val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
            val entitiesToUpsert = toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
            localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
            return
        }

        val localTimestamp = localTimestampGetter() ?: 0
        val remoteTimestamp = remoteTimestampGetter(userId) ?: 0

        when {
            // Local data is newer, save locally and synchronize from local to remote
            localTimestamp > remoteTimestamp -> {
                val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
                val entitiesToUpsert = toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entitiesToDelete + entitiesToUpsert, timestamp)

                val entities = localDataAfterTimestampGetter(remoteTimestamp)
                val dtos = entities.map(entityToCommandDtoMapper)
                val result = remoteSoftCommand(dtos, timestamp, userId)

                if (result) {
                    localDeleteCommand(entities.filter(entityDeletedPredicate))
                }
            }
            // Remote data is newer, save remotely and synchronize from remote to local
            localTimestamp < remoteTimestamp -> {
                val dtos = toDelete.map { dataModelToCommandDtoMapper(it, timestamp, true) } +
                        toUpsert.map { dataModelToCommandDtoMapper(it, timestamp, false) }
                val entities = remoteSoftCommandAndDataAfterTimestampGetter(
                    dtos, timestamp, userId, localTimestamp
                )?.map(queryDtoToEntityMapper)

                if (entities != null) {
                    val (entitiesToDelete, entitiesToUpsert) = entities.partition(entityDeletedPredicate)
                    localHardCommand(entitiesToDelete, entitiesToUpsert, timestamp)
                } else {
                    val entities = toDelete.map { dataModelToEntityMapper(it, timestamp, true) } +
                            toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                    localSoftCommand(entities, timestamp)
                }
            }
            // Data is up to date, save locally and remotely
            else -> {
                val entitiesToDelete = toDelete.map { dataModelToEntityMapper(it, timestamp, true) }
                val entitiesToUpsert = toUpsert.map { dataModelToEntityMapper(it, timestamp, false) }
                localSoftCommand(entitiesToDelete + entitiesToUpsert, timestamp)

                val dtos = toDelete.map { dataModelToCommandDtoMapper(it, timestamp, true) } +
                        toUpsert.map { dataModelToCommandDtoMapper(it, timestamp, false) }
                val result = remoteSoftCommand(dtos, timestamp, userId)

                if (result) {
                    localDeleteCommand(entitiesToDelete)
                }
            }
        }
    }

}
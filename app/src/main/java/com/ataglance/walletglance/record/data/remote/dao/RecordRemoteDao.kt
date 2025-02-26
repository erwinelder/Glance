package com.ataglance.walletglance.record.data.remote.dao

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.ataglance.walletglance.record.data.mapper.convertTransferToRecord
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity

class RecordRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<RecordRemoteEntity>
) {

    suspend fun upsertRecords(records: List<RecordRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = records, userId = userId)
    }

    suspend fun synchroniseRecords(
        recordsToSync: EntitiesToSync<RecordRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(
            toDelete = recordsToSync.toDelete, toUpsert = recordsToSync.toUpsert, userId = userId
        )
    }

    suspend fun deleteRecordsByAccounts(
        accountIds: List<Int>,
        timestamp: Long,
        userId: String,
        ttl: Int = 4
    ) {
        val querySize = firestoreAdapter.processCollectionDocumentsInBatch(
            userId = userId,
            whereInField = "accountId",
            whereInValues = accountIds.map { it.toString() },
            documentDataTransform = { documentData ->
                documentData
                    .let(firestoreAdapter::mapDataToEntity)
                    .copy(updateTime = timestamp, deleted = true)
                    .let(firestoreAdapter::mapEntityToData)
            }
        )

        if (querySize == 500 && ttl > 0) {
            deleteRecordsByAccounts(
                accountIds = accountIds, timestamp = timestamp, userId = userId, ttl = ttl - 1
            )
        }
    }

    suspend fun convertTransfersToRecords(
        noteValues: List<String>,
        timestamp: Long,
        userId: String,
        ttl: Int = 4
    ) {
        val querySize = firestoreAdapter.processCollectionDocumentsInBatch(
            userId = userId,
            whereInField = "note",
            whereInValues = noteValues,
            documentDataTransform = { documentData ->
                documentData
                    .let(firestoreAdapter::mapDataToEntity)
                    .convertTransferToRecord(timestamp)
                    .let(firestoreAdapter::mapEntityToData)
            }
        )

        if (querySize == 500 && ttl > 0) {
            convertTransfersToRecords(
                noteValues = noteValues, timestamp = timestamp, userId = userId, ttl = ttl - 1
            )
        }
    }

    suspend fun getRecordsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<RecordRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}
package com.ataglance.walletglance.record.data.remote

import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.mapper.toMap
import com.ataglance.walletglance.record.mapper.toRecordEntity
import com.google.firebase.firestore.FirebaseFirestore

class RecordRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<RecordEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "records",
    tableName = TableName.Record,
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any?>::toRecordEntity,
    entityToDataMapper = RecordEntity::toMap
) {

    fun deleteRecordsByRecordNumbers(
        recordNumbers: List<Int>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val batch = firestore.batch()

        collectionRef.whereIn("recordNum", recordNumbers).get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.forEach { document ->
                    batch.softDelete(document.reference, timestamp)
                }
                batch.commit()
                    .addOnSuccessListener {
                        updateTime(timestamp)
                        onSuccessListener()
                    }
                    .addOnFailureListener(onFailureListener)
            }
            .addOnFailureListener(onFailureListener)
    }

    fun convertTransfersToRecords(
        noteValues: List<String>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val query = collectionRef.whereIn("note", noteValues)

        query.get()
            .addOnSuccessListener { documents ->
                val batch = firestore.batch()

                documents.forEach { document ->
                    val updatedRecord = document.data
                        .toRecordEntity()
                        .convertTransferToRecord()
                        .toMap(timestamp)

                    batch.update(document.reference, updatedRecord)
                }

                batch.commit()
                    .addOnSuccessListener {
                        updateTime(timestamp)
                        onSuccessListener()
                    }
                    .addOnFailureListener(onFailureListener)
            }
            .addOnFailureListener(onFailureListener)
    }

}
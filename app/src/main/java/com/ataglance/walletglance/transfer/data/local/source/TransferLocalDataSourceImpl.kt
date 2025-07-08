package com.ataglance.walletglance.transfer.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.transfer.data.local.dao.TransferLocalDao
import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import kotlinx.coroutines.flow.Flow

class TransferLocalDataSourceImpl(
    private val transferDao: TransferLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : TransferLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Transfer.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Transfer.name, timestamp = timestamp)
    }

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.Transfer.name)
    }

    override suspend fun saveTransfers(
        transfers: List<TransferEntity>,
        timestamp: Long
    ): List<TransferEntity> {
        return transferDao.saveTransfers(transfers = transfers).also {
            saveUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun deleteTransfers(
        transfers: List<TransferEntity>,
        timestamp: Long?
    ) {
        transferDao.deleteTransfers(transfers = transfers)
        timestamp?.let { saveUpdateTime(timestamp = it) }
    }

    override suspend fun deleteAndSaveTransfers(
        toDelete: List<TransferEntity>,
        toUpsert: List<TransferEntity>,
        timestamp: Long
    ): List<TransferEntity> {
        return transferDao.deleteAndSaveTransfers(toDelete = toDelete, toUpsert = toUpsert)
            .also { saveUpdateTime(timestamp = timestamp) }
    }

    override suspend fun getTransfersAfterTimestamp(timestamp: Long): List<TransferEntity> {
        return transferDao.getTransfersAfterTimestamp(timestamp = timestamp)
    }

    override suspend fun getTransfer(id: Long): TransferEntity? {
        return transferDao.getTransfer(id = id)
    }

    override fun getTransfersInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<TransferEntity>> {
        return transferDao.getTransfersInDateRangeAsFlow(from = from, to = to)
    }

    override suspend fun getTransfersInDateRange(
        from: Long,
        to: Long
    ): List<TransferEntity> {
        return transferDao.getTransfersInDateRange(from = from, to = to)
    }

    override suspend fun getTransfersByAccounts(accountIds: List<Int>): List<TransferEntity> {
        return transferDao.getTransfersByAccounts(accountIds = accountIds)
    }

    override suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double {
        return transferDao.getTotalExpensesInDateRangeByAccounts(
            from = from,
            to = to,
            accountIds = accountIds
        ) ?: 0.0
    }

}

fun getTransferLocalDataSource(appDatabase: AppDatabase): TransferLocalDataSource {
    return TransferLocalDataSourceImpl(
        transferDao = appDatabase.transferDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}

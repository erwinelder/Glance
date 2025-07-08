package com.ataglance.walletglance.account.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountLocalDao {

    @Insert
    suspend fun insertAccounts(accounts: List<AccountEntity>): List<Long>

    @Upsert
    suspend fun upsertAccounts(accounts: List<AccountEntity>)

    @Transaction
    suspend fun saveAccounts(accounts: List<AccountEntity>): List<AccountEntity> {
        val (toInsert, toUpsert) = accounts.partition { it.id == 0 }
        upsertAccounts(accounts = toUpsert)
        val insertedIds = insertAccounts(accounts = toInsert).map { it.toInt() }.toMutableList()

        return toUpsert + toInsert.mapNotNull { account ->
            insertedIds.removeFirstOrNull()?.let { id ->
                account.copy(id = id)
            }
        }
    }

    @Delete
    suspend fun deleteAccounts(accounts: List<AccountEntity>)

    @Transaction
    suspend fun deleteAndSaveAccounts(
        toDelete: List<AccountEntity>,
        toSave: List<AccountEntity>
    ): List<AccountEntity> {
        deleteAccounts(accounts = toDelete)
        return saveAccounts(accounts = toSave)
    }

    @Query("DELETE FROM account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM account WHERE timestamp > :timestamp")
    suspend fun getAccountsAfterTimestamp(timestamp: Long): List<AccountEntity>

    @Query("SELECT * FROM account WHERE id IN (:ids) AND deleted = 0")
    suspend fun getAccounts(ids: List<Int>): List<AccountEntity>

    @Query("SELECT * FROM account WHERE id = :id AND deleted = 0")
    suspend fun getAccount(id: Int): AccountEntity?

    @Query("SELECT * FROM account WHERE deleted = 0 ORDER BY orderNum ASC")
    fun getAllAccountsAsFlow(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE deleted = 0 ORDER BY orderNum ASC")
    suspend fun getAllAccounts(): List<AccountEntity>

}
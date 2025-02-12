package com.ataglance.walletglance.account.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountLocalDao {

    @Upsert
    suspend fun upsertAccounts(accounts: List<AccountEntity>)

    @Delete
    suspend fun deleteAccounts(accounts: List<AccountEntity>)

    @Transaction
    suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>
    ) {
        deleteAccounts(toDelete)
        upsertAccounts(toUpsert)
    }

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM Account ORDER BY orderNum ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM Account WHERE id IN (:ids)")
    suspend fun getAccounts(ids: List<Int>): List<AccountEntity>

    @Query("SELECT * FROM Account WHERE id = :id")
    suspend fun getAccount(id: Int): AccountEntity?

}
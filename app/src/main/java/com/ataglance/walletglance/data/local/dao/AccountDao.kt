package com.ataglance.walletglance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.data.local.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Upsert
    suspend fun upsertAccount(account: AccountEntity)

    @Upsert
    suspend fun upsertAccounts(accountsList: List<AccountEntity>)

    @Query("DELETE FROM Account WHERE id IN (:idList)")
    suspend fun deleteAccountsByIds(idList: List<Int>)

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM Account ORDER BY orderNum ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

}
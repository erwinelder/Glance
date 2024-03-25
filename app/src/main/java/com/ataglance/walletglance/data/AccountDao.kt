package com.ataglance.walletglance.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAccounts(accountsList: List<Account>)

    @Query("DELETE FROM Account WHERE id == :id")
    suspend fun deleteAccountById(id: Int)

    @Query("DELETE FROM Account WHERE id IN (:idList)")
    suspend fun deleteAccountsByIds(idList: List<Int>)

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM Account ORDER BY orderNum ASC")
    fun getAllAccounts(): Flow<List<Account>>

}
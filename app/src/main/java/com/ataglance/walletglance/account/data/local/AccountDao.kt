package com.ataglance.walletglance.account.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao : BaseDao<AccountEntity> {

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM Account ORDER BY orderNum ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

}
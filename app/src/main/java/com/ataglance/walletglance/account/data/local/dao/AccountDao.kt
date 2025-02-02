package com.ataglance.walletglance.account.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao : BaseLocalDao<AccountEntity> {

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()

    @Query("SELECT * FROM Account ORDER BY orderNum ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

}
package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.model.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun upsertAccounts(
        accountList: List<AccountEntity>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    )

    suspend fun deleteAllAccounts(
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    )

    fun getAllAccounts(
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ): Flow<List<AccountEntity>>

}
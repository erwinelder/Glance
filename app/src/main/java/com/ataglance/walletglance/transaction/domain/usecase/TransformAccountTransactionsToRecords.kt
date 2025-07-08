package com.ataglance.walletglance.transaction.domain.usecase

interface TransformAccountTransactionsToRecords {

    suspend fun execute(accountIds: List<Int>)

}
package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transfer
import kotlinx.coroutines.flow.Flow

interface GetTransfersInDateRangeUseCase {

    fun getAsFlow(range: TimestampRange): Flow<List<Transfer>>

    suspend fun get(range: TimestampRange): List<Transfer>

}
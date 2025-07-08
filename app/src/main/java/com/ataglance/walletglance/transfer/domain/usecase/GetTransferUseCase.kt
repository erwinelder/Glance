package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface GetTransferUseCase {

    suspend fun get(id: Long): Transfer?

}
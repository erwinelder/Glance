package com.ataglance.walletglance.transfer.domain.usecase

interface DeleteTransferUseCase {

    suspend fun execute(transferId: Long)

}
package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.transfer.data.repository.TransferRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.mapper.toDomainModel

class GetTransferUseCaseImpl(
    private val transferRepository: TransferRepository
) : GetTransferUseCase {

    override suspend fun get(id: Long): Transfer? {
        return transferRepository.getTransfer(id = id)?.toDomainModel()
    }

}
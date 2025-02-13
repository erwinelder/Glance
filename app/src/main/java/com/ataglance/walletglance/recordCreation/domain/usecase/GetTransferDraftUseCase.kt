package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft

interface GetTransferDraftUseCase {
    suspend fun get(recordNum: Int?): TransferDraft
}
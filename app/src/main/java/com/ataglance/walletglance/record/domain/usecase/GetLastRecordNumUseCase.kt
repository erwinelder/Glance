package com.ataglance.walletglance.record.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetLastRecordNumUseCase {
    fun execute(): Flow<Int?>
}
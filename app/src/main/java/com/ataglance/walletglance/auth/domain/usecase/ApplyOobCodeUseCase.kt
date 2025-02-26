package com.ataglance.walletglance.auth.domain.usecase

interface ApplyOobCodeUseCase {
    suspend fun execute(obbCode: String): Boolean
}
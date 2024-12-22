package com.ataglance.walletglance.auth.domain.usecase

interface ApplyObbCodeUseCase {
    suspend fun execute(obbCode: String): Boolean
}
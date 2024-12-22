package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface RequestPasswordResetUseCase {
    suspend fun execute(email: String): AuthResult
}
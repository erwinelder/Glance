package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface SendEmailVerificationEmailUseCase {
    suspend fun execute(): AuthResult
}
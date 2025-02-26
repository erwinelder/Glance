package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface SetNewPasswordUseCase {
    suspend fun execute(obbCode: String, newPassword: String): AuthResult
}
package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface UpdatePasswordUseCase {
    suspend fun execute(currentPassword: String, newPassword: String): AuthResult
}
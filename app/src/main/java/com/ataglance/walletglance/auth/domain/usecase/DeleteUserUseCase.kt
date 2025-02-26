package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface DeleteUserUseCase {
    suspend fun execute(password: String): AuthResult
}
package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult

interface RequestEmailUpdateUseCase {
    suspend fun execute(password: String, newEmail: String): AuthResult
}
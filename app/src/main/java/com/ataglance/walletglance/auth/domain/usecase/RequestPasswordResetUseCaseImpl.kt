package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class RequestPasswordResetUseCaseImpl(
    private val authRepository: AuthRepository
) : RequestPasswordResetUseCase {
    override suspend fun execute(email: String): Result<AuthSuccess, AuthError> {
        return authRepository.requestPasswordReset(email = email)
    }
}
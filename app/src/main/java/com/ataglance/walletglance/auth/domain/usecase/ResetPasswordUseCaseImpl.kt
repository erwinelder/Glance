package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.ResetPasswordRequestDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class ResetPasswordUseCaseImpl(
    private val authRepository: AuthRepository
) : ResetPasswordUseCase {
    override suspend fun execute(
        oobCode: String,
        newPassword: String
    ): Result<AuthSuccess, AuthError> {
        val request = ResetPasswordRequestDto(oobCode = oobCode, newPassword = newPassword)
        return authRepository.verifyPasswordReset(resetPasswordRequest = request)
    }
}
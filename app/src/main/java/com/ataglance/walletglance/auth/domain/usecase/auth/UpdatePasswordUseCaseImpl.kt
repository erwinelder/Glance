package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.model.UpdatePasswordRequestDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class UpdatePasswordUseCaseImpl(
    private val authRepository: AuthRepository
) : UpdatePasswordUseCase {
    override suspend fun execute(
        password: String,
        newPassword: String
    ): Result<AuthSuccess, AuthError> {
        val request = UpdatePasswordRequestDto(password = password, newPassword = newPassword)
        return authRepository.updatePassword(updatePasswordRequest = request)
    }
}
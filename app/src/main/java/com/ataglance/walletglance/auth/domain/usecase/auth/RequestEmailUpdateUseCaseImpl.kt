package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.model.EmailUpdateRequestDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.request.domain.model.result.Result

class RequestEmailUpdateUseCaseImpl(
    private val authRepository: AuthRepository
) : RequestEmailUpdateUseCase {
    override suspend fun execute(
        password: String,
        newEmail: String
    ): Result<AuthSuccess, AuthError> {
        val request = EmailUpdateRequestDto(password = password, newEmail = newEmail)
        return authRepository.requestEmailUpdate(emailUpdateRequest = request)
    }
}
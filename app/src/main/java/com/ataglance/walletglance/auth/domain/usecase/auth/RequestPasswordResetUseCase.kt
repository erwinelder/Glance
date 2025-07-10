package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.request.domain.model.result.Result

interface RequestPasswordResetUseCase {
    suspend fun execute(email: String): Result<AuthSuccess, AuthError>
}
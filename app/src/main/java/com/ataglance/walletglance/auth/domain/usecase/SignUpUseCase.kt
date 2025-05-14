package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

interface SignUpUseCase {
    suspend fun execute(
        name: String,
        email: String,
        password: String
    ): Result<AuthSuccess, AuthError>
}
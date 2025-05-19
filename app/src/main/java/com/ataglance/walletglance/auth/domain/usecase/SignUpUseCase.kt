package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

interface SignUpUseCase {
    suspend fun execute(
        name: String,
        email: String,
        password: String
    ): Result<AuthSuccess, AuthError>
}
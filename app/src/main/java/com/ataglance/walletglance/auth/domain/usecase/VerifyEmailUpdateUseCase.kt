package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

interface VerifyEmailUpdateUseCase {
    suspend fun execute(oobCode: String): Result<AuthSuccess, AuthError>
}
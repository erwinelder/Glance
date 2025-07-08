package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

interface ResetPasswordUseCase {
    suspend fun execute(oobCode: String, newPassword: String): Result<AuthSuccess, AuthError>
}
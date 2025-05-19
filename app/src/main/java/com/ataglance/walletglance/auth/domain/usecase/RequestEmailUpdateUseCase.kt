package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

interface RequestEmailUpdateUseCase {
    suspend fun execute(password: String, newEmail: String): Result<AuthSuccess, AuthError>
}
package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface CreateNewUserUseCase {
    suspend fun execute(
        email: String,
        password: String,
        appLanguageCode: String
    ): ResultData<String, AuthError>
}
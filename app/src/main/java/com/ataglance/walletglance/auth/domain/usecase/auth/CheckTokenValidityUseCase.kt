package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.request.domain.model.result.ResultData

interface CheckTokenValidityUseCase {
    suspend fun execute(): ResultData<Unit, AuthError>
}
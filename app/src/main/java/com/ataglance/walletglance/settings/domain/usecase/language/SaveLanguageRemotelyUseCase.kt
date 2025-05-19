package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface SaveLanguageRemotelyUseCase {
    suspend fun execute(langCode: String, timestamp: Long): ResultData<Unit, AuthError>
}
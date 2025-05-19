package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.auth.data.model.SaveLanguageRequestDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

class SaveLanguageRemotelyUseCaseImpl(
    private val authRepository: AuthRepository
) : SaveLanguageRemotelyUseCase {
    override suspend fun execute(langCode: String, timestamp: Long): ResultData<Unit, AuthError> {
        val request = SaveLanguageRequestDto(langCode = langCode, timestamp = timestamp)
        return authRepository.saveLanguage(saveLanguageRequest = request)
    }
}
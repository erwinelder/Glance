package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.auth.data.model.SaveLanguageRequestDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.request.domain.model.result.ResultData
import com.ataglance.walletglance.settings.errorHandling.SettingsError

class SaveLanguageRemotelyUseCaseImpl(
    private val authRepository: AuthRepository
) : SaveLanguageRemotelyUseCase {
    override suspend fun execute(langCode: String, timestamp: Long): ResultData<Unit, SettingsError> {
        val request = SaveLanguageRequestDto(langCode = langCode, timestamp = timestamp)

        return authRepository.saveLanguage(saveLanguageRequest = request).mapError {
            SettingsError.NotSaved
        }
    }
}
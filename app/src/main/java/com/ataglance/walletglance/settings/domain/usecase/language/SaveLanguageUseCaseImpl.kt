package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.core.utils.getCurrentEpochTimestamp

class SaveLanguageUseCaseImpl(
    private val saveLanguageLocallyUseCase: SaveLanguageLocallyUseCase,
    private val saveLanguageRemotelyUseCase: SaveLanguageRemotelyUseCase,
    private val userContext: UserContext
) : SaveLanguageUseCase {
    override suspend fun execute(langCode: String) {
        val timestamp = getCurrentEpochTimestamp()

        saveLanguageLocallyUseCase.execute(langCode = langCode, timestamp = timestamp)

        if (userContext.isSignedIn()) {
            saveLanguageRemotelyUseCase.execute(langCode = langCode, timestamp = timestamp)
        }
    }
}
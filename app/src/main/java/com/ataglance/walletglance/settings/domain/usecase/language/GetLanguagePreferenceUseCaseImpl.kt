package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GetLanguagePreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetLanguagePreferenceUseCase {

    override fun getFlow(): Flow<String> {
        return settingsRepository.language
    }

    override suspend fun get(): String {
        return settingsRepository.language.firstOrNull() ?: AppLanguage.English.languageCode
    }

}
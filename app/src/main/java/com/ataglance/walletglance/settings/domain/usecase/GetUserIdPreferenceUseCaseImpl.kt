package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GetUserIdPreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetUserIdPreferenceUseCase {

    override fun getAsFlow(): Flow<String?> {
        return settingsRepository.userId
    }

    override suspend fun get(): String? {
        return settingsRepository.userId.firstOrNull()
    }

}
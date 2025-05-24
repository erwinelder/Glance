package com.ataglance.walletglance.auth.domain.usecase.user

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GetUserProfileLocalTimestampUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetUserProfileLocalTimestampUseCase {

    override suspend fun get(): Long {
        return settingsRepository.userProfileTimestamp.firstOrNull() ?: 0
    }

    override fun getAsFlow(): Flow<Long> {
        return settingsRepository.userProfileTimestamp
    }

}
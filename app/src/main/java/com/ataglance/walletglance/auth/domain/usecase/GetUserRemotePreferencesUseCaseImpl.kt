package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

class GetUserRemotePreferencesUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserRemotePreferencesUseCase {
    override suspend fun execute(userId: String): ResultData<UserRemotePreferences, AuthError> {
        return userRepository.getUserPreferences(userId)
    }
}
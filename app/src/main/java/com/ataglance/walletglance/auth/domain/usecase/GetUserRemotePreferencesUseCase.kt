package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface GetUserRemotePreferencesUseCase {
    suspend fun execute(userId: String): ResultData<UserRemotePreferences, AuthError>
}
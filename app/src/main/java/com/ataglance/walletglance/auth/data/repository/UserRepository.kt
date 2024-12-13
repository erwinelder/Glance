package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserRemotePreferences
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface UserRepository {

    suspend fun getUserPreferences(userId: String): ResultData<UserRemotePreferences, AuthError>

    suspend fun saveUserPreferences(userPreferences: UserRemotePreferences)

    suspend fun updateUserSubscription(userId: String, subscription: String)

    suspend fun deleteAllUserData(userId: String)

}
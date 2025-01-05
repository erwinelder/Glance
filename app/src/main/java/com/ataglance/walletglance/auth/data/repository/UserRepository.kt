package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface UserRepository {

    suspend fun getUserData(userId: String): ResultData<UserData, AuthError>

    suspend fun saveUserPreferences(userPreferences: UserData)

    suspend fun updateUserSubscription(userId: String, subscription: String)

    suspend fun deleteAllUserData(userId: String)

}
package com.ataglance.walletglance.auth.data.repository

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface UserRepository {

    suspend fun getUserData(userId: String): ResultData<UserData, AuthError>

    suspend fun saveUserData(userData: UserData)

    suspend fun updateUserSubscription(userId: String, subscription: String)

    suspend fun deleteAllUserData(userId: String)

}
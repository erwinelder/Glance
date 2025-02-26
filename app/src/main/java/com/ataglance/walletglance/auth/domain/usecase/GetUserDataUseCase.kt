package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

interface GetUserDataUseCase {
    suspend fun get(userId: String): ResultData<UserData, AuthError>
}
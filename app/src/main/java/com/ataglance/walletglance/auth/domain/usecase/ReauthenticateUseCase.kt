package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.FirebaseUser

interface ReauthenticateUseCase {
    suspend fun execute(password: String): ResultData<FirebaseUser, AuthError>
}
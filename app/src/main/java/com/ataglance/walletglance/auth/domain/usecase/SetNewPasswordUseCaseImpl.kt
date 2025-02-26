package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.tasks.await

class SetNewPasswordUseCaseImpl(private val auth: FirebaseAuth) : SetNewPasswordUseCase {
    override suspend fun execute(obbCode: String, newPassword: String): AuthResult {
        return try {
            auth.confirmPasswordReset(obbCode, newPassword).await()
            Result.Success(AuthSuccess.PasswordUpdated)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.InvalidCode)
                else -> Result.Error(AuthError.PasswordResetError)
            }
        }

    }
}
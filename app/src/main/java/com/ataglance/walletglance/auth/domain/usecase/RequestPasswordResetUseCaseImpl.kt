package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await

class RequestPasswordResetUseCaseImpl(
    private val auth: FirebaseAuth
) : RequestPasswordResetUseCase {
    override suspend fun execute(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.Success(AuthSuccess.ResetPasswordEmailSent)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> Result.Error(AuthError.UserNotFound)
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.InvalidEmail)
                else -> Result.Error(AuthError.EmailForPasswordResetError)
            }
        }
    }
}
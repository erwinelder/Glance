package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class SendEmailVerificationEmailUseCaseImpl(
    private val auth: FirebaseAuth
) : SendEmailVerificationEmailUseCase {
    override suspend fun execute(): AuthResult {
        val currentUser = auth.currentUser ?: return Result.Error(AuthError.UserNotSignedIn)

        return try {
            currentUser.sendEmailVerification().await()
            Result.Success(AuthSuccess.SignUpEmailVerificationSent)
        } catch (e: Exception) {
            Result.Error(AuthError.SignUpEmailVerificationError)
        }
    }
}
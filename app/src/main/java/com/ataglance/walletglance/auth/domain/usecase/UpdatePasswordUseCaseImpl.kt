package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.tasks.await

class UpdatePasswordUseCaseImpl(
    private val reauthenticateUseCase: ReauthenticateUseCase
) : UpdatePasswordUseCase {
    override suspend fun execute(currentPassword: String, newPassword: String): AuthResult {
        val reauthenticationResult = reauthenticateUseCase.execute(currentPassword)
        if (reauthenticationResult is ResultData.Error) {
            return Result.Error(reauthenticationResult.error)
        }
        val firebaseUser = (reauthenticationResult as ResultData.Success).data
        val email = firebaseUser.email ?: return Result.Error(AuthError.UserNotFound)

        try {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            firebaseUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            return Result.Error(AuthError.WrongCredentials)
        }

        return try {
            firebaseUser.updatePassword(newPassword).await()
            Result.Success(AuthSuccess.PasswordUpdated)
        } catch (e: Exception) {
            Result.Error(AuthError.UpdatePasswordError)
        }
    }
}
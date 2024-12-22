package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.core.utils.takeIfNoneIsNull
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await

class RequestEmailUpdateUseCaseImpl(private val auth: FirebaseAuth) : RequestEmailUpdateUseCase {
    override suspend fun execute(password: String, newEmail: String): AuthResult {
        val (currentUser, currentEmail) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return Result.Error(AuthError.UserNotSignedIn)

        try {
            val credential = EmailAuthProvider.getCredential(currentEmail, password)
            currentUser.reauthenticate(credential).await()
        } catch (e: Exception) {
            return when (e) {
                is FirebaseAuthInvalidCredentialsException -> Result.Error(AuthError.WrongCredentials)
                is FirebaseAuthInvalidUserException -> Result.Error(AuthError.UserNotFound)
                else -> Result.Error(AuthError.ReauthenticationError)
            }
        }

        return try {
            currentUser.verifyBeforeUpdateEmail(newEmail).await()
            Result.Success(AuthSuccess.UpdateEmailEmailVerificationSent)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthEmailException, is FirebaseAuthInvalidCredentialsException ->
                    Result.Error(AuthError.InvalidEmail)
                is FirebaseAuthUserCollisionException -> Result.Error(AuthError.UserAlreadyExists)
                else -> Result.Error(AuthError.EmailVerificationError)
            }
        }
    }
}
package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class SignInUseCaseImpl(private val auth: FirebaseAuth) : SignInUseCase {
    override suspend fun execute(
        email: String,
        password: String
    ): ResultData<FirebaseUser, AuthError> {
        try {
            return auth.signInWithEmailAndPassword(email, password).await()
                .user?.let { ResultData.Success(it) }
                ?: return ResultData.Error(AuthError.WrongCredentials)
        } catch (e: Exception) {
            return when (e) {
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseAuthInvalidCredentialsException -> ResultData.Error(AuthError.WrongCredentials)
                is FirebaseAuthInvalidUserException -> ResultData.Error(AuthError.UserNotFound)
                else -> ResultData.Error(AuthError.SignInError)
            }
        }
    }
}
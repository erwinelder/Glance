package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.core.utils.takeIfNoneIsNull
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class ReauthenticateUseCaseImpl(private val auth: FirebaseAuth) : ReauthenticateUseCase {
    override suspend fun execute(password: String): ResultData<FirebaseUser, AuthError> {
        val (currentUser, email) = auth.currentUser.let { it to it?.email }.takeIfNoneIsNull()
            ?: return ResultData.Error(AuthError.UserNotSignedIn)

        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).await()
            auth.currentUser?.reload()?.await()

            auth.currentUser
                ?.let { ResultData.Success(it) }
                ?: ResultData.Error(AuthError.UserNotSignedIn)
        } catch (e: Exception) {
            ResultData.Error(AuthError.WrongCredentials)
        }
    }
}
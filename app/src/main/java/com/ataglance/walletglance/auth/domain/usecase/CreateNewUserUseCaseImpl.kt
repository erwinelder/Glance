package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class CreateNewUserUseCaseImpl(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : CreateNewUserUseCase {
    override suspend fun execute(
        email: String,
        password: String,
        appLanguageCode: String
    ): ResultData<String, AuthError> {
        return try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await()?.user
                ?: return ResultData.Error(AuthError.UserNotCreated)

            val userPreferences = UserData(
                userId = firebaseUser.uid,
                language = appLanguageCode,
                subscription = AppSubscription.Free
            )
            userRepository.saveUserData(userPreferences)

            ResultData.Success(firebaseUser.uid)
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthUserCollisionException -> ResultData.Error(AuthError.UserAlreadyExists)
                is FirebaseAuthEmailException -> ResultData.Error(AuthError.InvalidEmail)
                is FirebaseFirestoreException -> ResultData.Error(AuthError.UserDataNotSaved)
                else -> ResultData.Error(AuthError.UserNotCreated)
            }
        }
    }
}
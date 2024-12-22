package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.auth.domain.model.AuthResult
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class DeleteUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val reauthenticateUseCase: ReauthenticateUseCase,
) : DeleteUserUseCase {
    override suspend fun execute(password: String): AuthResult {
        val reauthenticationResult = reauthenticateUseCase.execute(password)
        if (reauthenticationResult is ResultData.Error) {
            return Result.Error(reauthenticationResult.error)
        }
        val firebaseUser = (reauthenticationResult as ResultData.Success).data

        return try {
            userRepository.deleteAllUserData(firebaseUser.uid)

            firebaseUser.delete().await()

            Result.Success(AuthSuccess.AccountDeleted)
        } catch (e: Exception) {
            when (e) {
                is FirebaseFirestoreException -> Result.Error(AuthError.DataDeletionError)
                else -> Result.Error(AuthError.AccountDeletionError)
            }
        }
    }
}
package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class CheckEmailVerificationUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : CheckEmailVerificationUseCase {
    override suspend fun execute(email: String, password: String): Result<AuthSuccess, AuthError> {
        val userCredentials = UserCredentialsDto(email = email, password = password)
        val result = authRepository.signInWithEmailAndPassword(userCredentials = userCredentials)

        result.getDataIfSuccess()?.let { data ->
            val user = data.toDomainModel() ?: return Result.Error(AuthError.RequestDataNotValid)
            userContext.saveUserWithToken(user = user)
        }

        return result.toDefaultResult(success = AuthSuccess.SignedUp)
    }
}
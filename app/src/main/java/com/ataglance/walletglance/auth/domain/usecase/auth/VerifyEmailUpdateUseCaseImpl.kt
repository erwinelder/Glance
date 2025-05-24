package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class VerifyEmailUpdateUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : VerifyEmailUpdateUseCase {
    override suspend fun execute(oobCode: String): Result<AuthSuccess, AuthError> {
        val result = authRepository.verifyEmailUpdate(oobCode = oobCode)

        result.getDataIfSuccess()?.let { data ->
            val user = data.toDomainModel() ?: return Result.Error(AuthError.RequestDataNotValid)
            userContext.saveUserWithToken(user = user)
        }

        return result.toDefaultResult(success = AuthSuccess.EmailUpdated)
    }
}
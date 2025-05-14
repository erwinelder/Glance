package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.auth.mapper.toDomainModel
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class FinishSignUpUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext
) : FinishSignUpUseCase {
    override suspend fun execute(
        oobCode: String
    ): Result<AuthSuccess, AuthError> {
        val result = authRepository.finishSignUpWithEmailAndPassword(oobCode = oobCode)

        result.getDataIfSuccess()?.let { data ->
            val user = data.toDomainModel() ?: return Result.Error(AuthError.DataNotValid)
            userContext.saveUserWithToken(user = user)
        }

        return result.toDefaultResult(success = AuthSuccess.SignedUp)
    }
}
package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserCredentialsDto
import com.ataglance.walletglance.auth.data.repository.AuthRepository
import com.ataglance.walletglance.auth.domain.model.UserContext
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthSuccess
import com.ataglance.walletglance.errorHandling.domain.model.result.Result

class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userContext: UserContext,
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase
) : DeleteAccountUseCase {
    override suspend fun execute(password: String): Result<AuthSuccess, AuthError> {
        val email = userContext.email ?: return Result.Error(AuthError.UserNotSignedIn)
        val credentials = UserCredentialsDto(email = email, password = password)

        val result = authRepository.deleteAccount(userCredentials = credentials)

        if (result is Result.Success) {
            userContext.deleteData()
            deleteAllDataLocallyUseCase.execute()
        }

        return result
    }
}
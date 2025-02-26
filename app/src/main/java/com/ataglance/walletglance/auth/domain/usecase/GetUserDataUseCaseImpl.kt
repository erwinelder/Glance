package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.errorHandling.domain.model.result.AuthError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData

class GetUserDataUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserDataUseCase {
    override suspend fun get(userId: String): ResultData<UserData, AuthError> {
        return userRepository.getUserData(userId)
    }
}
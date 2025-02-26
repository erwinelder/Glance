package com.ataglance.walletglance.billing.domain.usecase

import com.ataglance.walletglance.auth.data.repository.UserRepository

class UpdateUserSubscriptionUseCaseImpl(
    private val userRepository: UserRepository
) : UpdateUserSubscriptionUseCase {
    override suspend fun execute(userId: String, subscription: String) {
        userRepository.updateUserSubscription(userId, subscription)
    }
}
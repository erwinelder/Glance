package com.ataglance.walletglance.billing.domain.usecase

interface UpdateUserSubscriptionUseCase {
    suspend fun execute(userId: String, subscription: String)
}
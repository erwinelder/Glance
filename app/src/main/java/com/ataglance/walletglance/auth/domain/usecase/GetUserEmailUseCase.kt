package com.ataglance.walletglance.auth.domain.usecase

interface GetUserEmailUseCase {
    fun execute(): String?
}
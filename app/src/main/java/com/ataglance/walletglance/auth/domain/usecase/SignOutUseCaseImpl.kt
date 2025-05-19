package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.auth.domain.model.user.UserContext

class SignOutUseCaseImpl(
    private val userContext: UserContext
) : SignOutUseCase {
    override fun execute() {
        userContext.deleteData()
    }
}
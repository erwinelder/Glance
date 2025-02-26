package com.ataglance.walletglance.auth.domain.usecase

import com.google.firebase.auth.FirebaseAuth

class UserEmailIsVerifiedUseCaseImpl(private val auth: FirebaseAuth) : UserEmailIsVerifiedUseCase {
    override fun execute(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }
}
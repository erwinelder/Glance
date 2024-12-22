package com.ataglance.walletglance.auth.domain.usecase

import com.google.firebase.auth.FirebaseAuth

class GetUserEmailUseCaseImpl(private val auth: FirebaseAuth) : GetUserEmailUseCase {
    override fun execute(): String? {
        return auth.currentUser?.email
    }
}
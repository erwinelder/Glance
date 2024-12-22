package com.ataglance.walletglance.auth.domain.usecase

import com.google.firebase.auth.FirebaseAuth

class SignOutUseCaseImpl(private val auth: FirebaseAuth) : SignOutUseCase {
    override fun execute() {
        auth.signOut()
    }
}
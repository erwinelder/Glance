package com.ataglance.walletglance.auth.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ApplyOobCodeUseCaseImpl(private val auth: FirebaseAuth) : ApplyOobCodeUseCase {
    override suspend fun execute(obbCode: String): Boolean {
        return try {
            auth.applyActionCode(obbCode).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
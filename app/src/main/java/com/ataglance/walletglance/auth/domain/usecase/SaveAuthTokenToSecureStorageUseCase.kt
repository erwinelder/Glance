package com.ataglance.walletglance.auth.domain.usecase

interface SaveAuthTokenToSecureStorageUseCase {
    fun execute(token: String)
}
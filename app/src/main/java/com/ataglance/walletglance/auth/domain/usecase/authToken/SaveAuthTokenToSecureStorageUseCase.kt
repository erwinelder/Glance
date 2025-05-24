package com.ataglance.walletglance.auth.domain.usecase.authToken

interface SaveAuthTokenToSecureStorageUseCase {
    fun execute(token: String)
}
package com.ataglance.walletglance.settings.domain.usecase.authToken

interface SaveAuthTokenToSecureStorageUseCase {
    fun execute(token: String)
}
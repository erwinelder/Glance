package com.ataglance.walletglance.auth.domain.usecase

import com.ataglance.walletglance.core.data.local.preferences.SecureStorage

class SaveAuthTokenToSecureStorageUseCaseImpl(
    private val secureStorage: SecureStorage
) : SaveAuthTokenToSecureStorageUseCase {
    override fun execute(token: String) {
        secureStorage.saveAuthToken(token = token)
    }
}
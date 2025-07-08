package com.ataglance.walletglance.auth.domain.usecase.authToken

import com.ataglance.walletglance.core.data.local.preferences.SecureStorage

class GetAuthTokenFromSecureStorageUseCaseImpl(
    private val secureStorage: SecureStorage
) : GetAuthTokenFromSecureStorageUseCase {
    override fun execute(): String? {
        return secureStorage.getAuthToken()
    }
}
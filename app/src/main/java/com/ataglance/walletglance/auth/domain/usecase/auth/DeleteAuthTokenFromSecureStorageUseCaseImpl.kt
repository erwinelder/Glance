package com.ataglance.walletglance.auth.domain.usecase.auth

import com.ataglance.walletglance.core.data.local.preferences.SecureStorage

class DeleteAuthTokenFromSecureStorageUseCaseImpl(
    private val secureStorage: SecureStorage
) : DeleteAuthTokenFromSecureStorageUseCase {
    override fun execute() {
        secureStorage.deleteAuthToken()
    }
}
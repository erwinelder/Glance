package com.ataglance.walletglance.settings.domain.usecase.authToken

interface GetAuthTokenFromSecureStorageUseCase {
    fun execute(): String?
}
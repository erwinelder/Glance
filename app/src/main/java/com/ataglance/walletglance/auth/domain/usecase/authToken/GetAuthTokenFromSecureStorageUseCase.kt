package com.ataglance.walletglance.auth.domain.usecase.authToken

interface GetAuthTokenFromSecureStorageUseCase {
    fun execute(): String?
}
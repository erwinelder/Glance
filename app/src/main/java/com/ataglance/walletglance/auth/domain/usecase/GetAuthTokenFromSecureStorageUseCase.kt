package com.ataglance.walletglance.auth.domain.usecase

interface GetAuthTokenFromSecureStorageUseCase {
    fun execute(): String?
}
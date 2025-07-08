package com.ataglance.walletglance.auth.domain.usecase.authToken

class GetAuthTokenFromSecureStorageUseCaseMock : GetAuthTokenFromSecureStorageUseCase {

    var token: String? = null

    companion object {

        fun returnsNull(): GetAuthTokenFromSecureStorageUseCaseMock {
            return GetAuthTokenFromSecureStorageUseCaseMock().apply {
                token = null
            }
        }

        fun returnsToken(token: String? = ""): GetAuthTokenFromSecureStorageUseCaseMock {
            return GetAuthTokenFromSecureStorageUseCaseMock().apply {
                this.token = token
            }
        }

    }


    override fun execute(): String? {
        return token
    }

}
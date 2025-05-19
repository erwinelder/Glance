package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.auth.domain.usecase.DeleteAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.GetAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.SaveAuthTokenToSecureStorageUseCase
import com.ataglance.walletglance.billing.domain.model.AppSubscription

class UserContext(
    private val getAuthTokenFromSecureStorageUseCase: GetAuthTokenFromSecureStorageUseCase,
    private val saveAuthTokenToSecureStorageUseCase: SaveAuthTokenToSecureStorageUseCase,
    private val deleteAuthTokenFromSecureStorageUseCase: DeleteAuthTokenFromSecureStorageUseCase
) { // TODO: refactor

    var userId: Int? = null
        private set
    var email: String? = null
        private set
    var name: String? = null
        private set
    var role: UserRole = UserRole.User
        private set
    var subscription: AppSubscription = AppSubscription.Free
        private set


    fun isSignedIn(): Boolean = userId != null
    fun isAdmin(): Boolean = role == UserRole.Admin

    fun getAuthToken(): String? {
        return getAuthTokenFromSecureStorageUseCase.execute()
    }

    fun saveUser(user: User) {
        this.userId = user.id
        this.email = user.email
        this.name = user.name
        this.role = user.role
        this.subscription = user.subscription
    }

    fun saveUserWithToken(user: UserWithToken) {
        this.userId = user.id
        this.email = user.email
        this.name = user.name
        this.role = user.role
        this.subscription = user.subscription

        saveAuthTokenToSecureStorageUseCase.execute(token = user.token)
    }

    fun deleteData() {
        userId = null
        email = null
        name = null
        role = UserRole.User
        subscription = AppSubscription.Free

        deleteAuthTokenFromSecureStorageUseCase.execute()
    }



    private var userIdString: String? = null

    fun getUserIdOld(): String? {
        return userIdString
    }


    fun isEligibleForDataSync(): Boolean {
        return isSignedIn() && subscription != AppSubscription.Free
    }

    fun getUserIdIfEligibleForDataSync(): String? {
        return if (isEligibleForDataSync()) userIdString else null
    }

}
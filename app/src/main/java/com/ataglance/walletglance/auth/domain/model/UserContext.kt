package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.auth.domain.usecase.GetAuthTokenFromSecureStorageUseCase
import com.ataglance.walletglance.auth.domain.usecase.SaveAuthTokenToSecureStorageUseCase
import com.ataglance.walletglance.billing.domain.model.AppSubscription

class UserContext(
    private val getAuthTokenFromSecureStorageUseCase: GetAuthTokenFromSecureStorageUseCase,
    private val saveAuthTokenToSecureStorageUseCase: SaveAuthTokenToSecureStorageUseCase
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


    fun isUser(): Boolean = userId != null // TODO: rename to isSignedIn
    fun isAdmin(): Boolean = role == UserRole.Admin

    fun getAuthToken(): String? {
        return getAuthTokenFromSecureStorageUseCase.execute()
    }

    fun saveUserWithToken(user: UserWithToken) {
        this.userId = user.id
        this.email = user.email
        this.name = user.name
        this.role = user.role
        this.subscription = user.subscription

        saveAuthTokenToSecureStorageUseCase.execute(token = user.token)
    }



    private var userIdString: String? = null

    fun isSignedInOld(): Boolean {
        return userIdString != null
    }

    fun getUserIdOld(): String? {
        return userIdString
    }

    fun setUserIdOld(userId: String) {
        this.userIdString = userId
    }

    fun resetUserIdOld() {
        userIdString = null
    }


    private var subscriptionOld = AppSubscription.Free

    fun setSubscriptionOld(subscription: AppSubscription) {
        this.subscriptionOld = subscription
    }

    fun resetSubscriptionOld() {
        subscriptionOld = AppSubscription.Free
    }


    fun isEligibleForDataSync(): Boolean {
        return isSignedInOld() && subscriptionOld != AppSubscription.Free
    }

    fun getUserIdIfEligibleForDataSync(): String? {
        return if (isEligibleForDataSync()) userIdString else null
    }

    fun resetUser() {
        resetUserIdOld()
        resetSubscriptionOld()
    }

}
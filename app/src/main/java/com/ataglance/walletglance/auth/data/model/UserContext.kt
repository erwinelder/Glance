package com.ataglance.walletglance.auth.data.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

class UserContext {

    private var userId: String? = null

    fun isSignedIn(): Boolean {
        return userId != null
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun resetUserId() {
        userId = null
    }


    private var subscription = AppSubscription.Free

    fun getSubscription(): AppSubscription {
        return subscription
    }

    fun setSubscription(subscription: AppSubscription) {
        this.subscription = subscription
    }

    fun resetSubscription() {
        subscription = AppSubscription.Free
    }


    fun isEligibleForDataSync(): Boolean {
        return isSignedIn() && subscription != AppSubscription.Free
    }

    fun getUserIdIfEligibleForDataSync(): String? {
        return if (isEligibleForDataSync()) userId else null
    }

    fun resetUser() {
        resetUserId()
        resetSubscription()
    }

}

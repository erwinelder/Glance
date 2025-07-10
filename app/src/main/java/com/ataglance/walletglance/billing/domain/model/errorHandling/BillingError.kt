package com.ataglance.walletglance.billing.domain.model.errorHandling

import com.ataglance.walletglance.request.domain.model.result.Error

enum class BillingError : Error {
    UserCancelledPurchase,
    UserNotSignedIn,
    NoNetwork,
    Unknown
}
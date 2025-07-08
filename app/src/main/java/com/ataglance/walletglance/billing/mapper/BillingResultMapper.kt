package com.ataglance.walletglance.billing.mapper

import androidx.annotation.StringRes
import com.android.billingclient.api.ProductDetails
import com.ataglance.walletglance.R
import com.ataglance.walletglance.billing.domain.model.errorHandling.BillingError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.errorHandling.presentation.model.ResultTitleWithMessageState

fun ResultData<ProductDetails, BillingError>.toResultState(): ResultTitleWithMessageState {
    return when (this) {
        is ResultData.Success -> ResultTitleWithMessageState(
            isSuccessful = true,
            titleRes = R.string.subscribed_successfully,
            messageRes = R.string.thank_you_for_subscribing
        )
        is ResultData.Error -> ResultTitleWithMessageState(
            isSuccessful = false,
            titleRes = this.error.asTitleRes(),
            messageRes = this.error.asMessageRes()
        )
    }
}

@StringRes
private fun BillingError.asTitleRes(): Int {
    return when (this) {
        BillingError.UserCancelledPurchase, BillingError.UserNotSignedIn, BillingError.NoNetwork,
        BillingError.Unknown ->
            R.string.oops
    }
}

@StringRes
private fun BillingError.asMessageRes(): Int {
    return when (this) {
        BillingError.UserCancelledPurchase -> R.string.purchase_was_cancelled
        BillingError.UserNotSignedIn -> R.string.user_not_signed_in_message
        BillingError.NoNetwork -> R.string.no_network
        BillingError.Unknown -> R.string.subscription_purchase_error
    }
}

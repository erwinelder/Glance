package com.ataglance.walletglance.errorHandling.mapper

import androidx.annotation.StringRes
import com.android.billingclient.api.ProductDetails
import com.ataglance.walletglance.R
import com.ataglance.walletglance.errorHandling.domain.model.result.BillingError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState

fun ResultData<ProductDetails, BillingError>.toUiState(): ResultUiState {
    return when (this) {
        is ResultData.Success -> ResultUiState(
            isSuccessful = true,
            titleRes = R.string.subscribed_successfully,
            messageRes = R.string.thank_you_for_subscribing
        )
        is ResultData.Error -> ResultUiState(
            isSuccessful = false,
            titleRes = this.error.asTitleRes(),
            messageRes = this.error.asMessageRes()
        )
    }
}

@StringRes
private fun BillingError.asTitleRes(): Int {
    return when (this) {
        BillingError.UserCancelledPurchase, BillingError.NoNetwork, BillingError.Unknown ->
            R.string.oops
    }
}

@StringRes
private fun BillingError.asMessageRes(): Int {
    return when (this) {
        BillingError.UserCancelledPurchase -> R.string.purchase_was_cancelled
        BillingError.NoNetwork -> R.string.no_network
        BillingError.Unknown -> R.string.subscription_purchase_error
    }
}

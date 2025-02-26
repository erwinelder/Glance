package com.ataglance.walletglance.billing.presentation.utils

import com.android.billingclient.api.ProductDetails

fun ProductDetails.getFormattedPrice(): String? {
    return subscriptionOfferDetails
        ?.firstOrNull()
        ?.pricingPhases
        ?.pricingPhaseList
        ?.firstOrNull()
        ?.formattedPrice
}
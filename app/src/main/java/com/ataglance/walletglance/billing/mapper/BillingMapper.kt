package com.ataglance.walletglance.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.ataglance.walletglance.billing.presentation.model.SubscriptionUiState
import com.ataglance.walletglance.billing.presentation.utils.getFormattedPrice

fun List<ProductDetails>.toSubscriptionUiStateList(): List<SubscriptionUiState> {
    return this.map { it.toSubscriptionUiState() }
}

fun ProductDetails.toSubscriptionUiState(): SubscriptionUiState {
    return SubscriptionUiState(
        id = this.productId,
        title = this.title,
        name = this.name,
        description = this.description,
        price = this.getFormattedPrice() ?: "---"
    )
}

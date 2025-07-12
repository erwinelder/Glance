package com.ataglance.walletglance.billing.mapper

import com.android.billingclient.api.ProductDetails
import com.ataglance.walletglance.billing.data.model.AppSubscriptionDto
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.billing.presentation.model.SubscriptionUiState
import com.ataglance.walletglance.billing.presentation.utils.getFormattedPrice


fun AppSubscriptionDto.toDomainModel(): AppSubscription {
    return when (this) {
        AppSubscriptionDto.Base -> AppSubscription.Base
        AppSubscriptionDto.Premium -> AppSubscription.Premium
    }
}


fun List<ProductDetails>.toSubscriptionUiStateList(): List<SubscriptionUiState> {
    return this.map { it.toSubscriptionUiState() }
}

fun ProductDetails.toSubscriptionUiState(): SubscriptionUiState {
    return SubscriptionUiState(
        id = this.productId,
        name = this.name,
        benefits = this.subscriptionOfferDetails?.map { it.basePlanId }.orEmpty(),
        price = this.getFormattedPrice() ?: "---"
    )
}

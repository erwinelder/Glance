package com.ataglance.walletglance.billing.presentation.model

data class SubscriptionUiState(
    val id: String,
    val name: String,
    val benefits: List<String>,
    val price: String
)

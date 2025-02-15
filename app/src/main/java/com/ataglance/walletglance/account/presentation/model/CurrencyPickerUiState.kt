package com.ataglance.walletglance.account.presentation.model

data class CurrencyPickerUiState(
    val isSearching: Boolean = false,
    val selectedCurrency: CurrencyItem?
)
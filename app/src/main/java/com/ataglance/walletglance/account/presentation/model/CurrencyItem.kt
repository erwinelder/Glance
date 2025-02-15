package com.ataglance.walletglance.account.presentation.model

import java.util.Currency

data class CurrencyItem(
    val currencyCode: String,
    val displayName: String
) {
    constructor(currency: Currency) : this(currency.currencyCode, currency.displayName)

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$currencyCode $displayName",
            "$currencyCode$displayName",
            currencyCode,
            displayName
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
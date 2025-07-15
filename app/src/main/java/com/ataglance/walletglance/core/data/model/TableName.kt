package com.ataglance.walletglance.core.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class TableName {
    Account, Category, Record, Transfer,
    CategoryCollection, Budget,
    BudgetOnWidget, Widget, NavigationButton
}
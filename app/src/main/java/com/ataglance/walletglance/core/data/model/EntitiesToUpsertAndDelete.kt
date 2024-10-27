package com.ataglance.walletglance.core.data.model

data class EntitiesToUpsertAndDelete<T>(
    val toUpsert: List<T> = emptyList(),
    val toDelete: List<T> = emptyList()
)

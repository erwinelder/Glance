package com.ataglance.walletglance.categoryCollection.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCollectionDto(
    val id: Int,
    val orderNum: Int,
    val type: Char,
    val name: String,
    val timestamp: Long,
    val deleted: Boolean
)

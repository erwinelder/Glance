package com.ataglance.walletglance.category.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryQueryDto(
    val userId: Int,
    val id: Int,
    val type: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String,
    val colorName: String,
    val timestamp: Long,
    val deleted: Boolean
)

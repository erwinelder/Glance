package com.ataglance.walletglance.category.data.remote.model

data class CategoryRemoteEntity(
    val updateTime: Long,
    val deleted: Boolean,
    val id: Int = 0,
    val type: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String,
    val colorName: String
)

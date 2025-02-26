package com.ataglance.walletglance.categoryCollection.data.remote.model

data class CategoryCollectionRemoteEntity(
    val updateTime: Long,
    val deleted: Boolean,
    val id: Int = 0,
    val orderNum: Int,
    val type: Char,
    val name: String
)

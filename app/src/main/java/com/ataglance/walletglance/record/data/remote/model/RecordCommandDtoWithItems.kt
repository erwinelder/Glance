package com.ataglance.walletglance.record.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RecordCommandDtoWithItems(
    val record: RecordCommandDto,
    val items: List<RecordItemDto>
) {

    val recordId: Long
        get() = record.id

    val deleted: Boolean
        get() = record.deleted

}

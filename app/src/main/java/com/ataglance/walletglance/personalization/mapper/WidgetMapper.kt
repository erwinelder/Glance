package com.ataglance.walletglance.personalization.mapper

import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import com.ataglance.walletglance.personalization.domain.model.WidgetName



fun Map<String, Any?>.toWidgetEntity(): WidgetEntity {
    return WidgetEntity(
        name = this["name"] as String,
        orderNum = this["orderNum"] as Int
    )
}

fun WidgetEntity.toMap(timestamp: Long): HashMap<String, Any> {
    return hashMapOf(
        "LMT" to timestamp,
        "name" to this.name,
        "orderNum" to this.orderNum
    )
}



fun WidgetEntity.toWidgetName(): WidgetName? {
    return enumValueOrNull<WidgetName>(this.name)
}

fun List<WidgetEntity>.toWidgetNamesList(): List<WidgetName> {
    return this.mapNotNull { it.toWidgetName() }
}



fun List<WidgetName>.toEntityList(): List<WidgetEntity> {
    return this.mapIndexed { index, widgetName ->
        WidgetEntity(name = widgetName.name, orderNum = index)
    }
}

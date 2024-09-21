package com.ataglance.walletglance.personalization.domain.mapper

import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.core.utils.enumValueOrNull



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

package com.ataglance.walletglance.personalization.mapper

import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.domain.model.WidgetName


fun List<WidgetEntity>.toDomainModels(): List<WidgetName> {
    return this.mapNotNull(WidgetEntity::toDomainModel)
}

fun WidgetEntity.toDomainModel(): WidgetName? {
    return enumValueOrNull<WidgetName>(name)
}


fun List<WidgetName>.toDataModels(): List<WidgetEntity> {
    return mapIndexed { index, widgetName ->
        WidgetEntity(name = widgetName.name, orderNum = index)
    }
}

package com.ataglance.walletglance.personalization.mapper

import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.personalization.data.model.WidgetDataModel
import com.ataglance.walletglance.personalization.domain.model.WidgetName


fun List<WidgetDataModel>.toDomainModelsSorted(): List<WidgetName> {
    return this
        .sortedBy { it.orderNum }
        .mapNotNull { it.toDomainModel() }
}

fun WidgetDataModel.toDomainModel(): WidgetName? {
    return enumValueOrNull<WidgetName>(name)
}


fun List<WidgetName>.toDataModels(): List<WidgetDataModel> {
    return mapIndexed { index, widgetName ->
        WidgetDataModel(name = widgetName.name, orderNum = index)
    }
}

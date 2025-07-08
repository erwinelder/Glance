package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.domain.model.WidgetName

interface SaveWidgetsUseCase {

    suspend fun saveAndDeleteRest(widgetsToSave: List<WidgetName>, currentWidgets: List<WidgetName>)

    suspend fun saveAndDeleteRest(widgets: List<WidgetName>)

}
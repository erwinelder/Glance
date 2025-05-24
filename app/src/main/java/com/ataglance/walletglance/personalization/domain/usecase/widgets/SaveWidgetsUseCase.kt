package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.domain.model.WidgetName

interface SaveWidgetsUseCase {
    suspend fun save(widgetsToSave: List<WidgetName>, currentWidgets: List<WidgetName>)
    suspend fun save(widgets: List<WidgetName>)
}
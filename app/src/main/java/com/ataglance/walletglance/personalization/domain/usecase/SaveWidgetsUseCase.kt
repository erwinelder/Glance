package com.ataglance.walletglance.personalization.domain.usecase

import com.ataglance.walletglance.personalization.domain.model.WidgetName

interface SaveWidgetsUseCase {
    suspend fun execute(widgetsToSave: List<WidgetName>, currentWidgets: List<WidgetName>)
}
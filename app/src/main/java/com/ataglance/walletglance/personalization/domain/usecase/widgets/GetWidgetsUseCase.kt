package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.domain.model.WidgetName
import kotlinx.coroutines.flow.Flow

interface GetWidgetsUseCase {
    fun getFlow(): Flow<List<WidgetName>>
}
package com.ataglance.walletglance.personalization.domain.usecase.widgets

import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.mapper.toDataModels
import com.ataglance.walletglance.personalization.mapper.toDomainModelsSorted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetWidgetsUseCaseImpl(
    private val widgetRepository: WidgetRepository
) : GetWidgetsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<WidgetName>> {
        return widgetRepository.getAllWidgetsAsFlow().mapLatest { widgets ->
            if (widgets.isEmpty()) {
                val defaultWidgetNamesList = getDefaultWidgetNames()
                widgetRepository.upsertWidgets(widgets = defaultWidgetNamesList.toDataModels())
                defaultWidgetNamesList
            } else {
                widgets.toDomainModelsSorted()
            }
        }
    }

    private fun getDefaultWidgetNames(): List<WidgetName> {
        return listOf(
            WidgetName.ChosenBudgets,
            WidgetName.TotalForPeriod,
            WidgetName.RecentRecords,
            WidgetName.TopExpenseCategories,
        )
    }

}
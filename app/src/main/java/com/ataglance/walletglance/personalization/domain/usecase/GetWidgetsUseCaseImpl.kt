package com.ataglance.walletglance.personalization.domain.usecase

import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.mapper.toDataModels
import com.ataglance.walletglance.personalization.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWidgetsUseCaseImpl(
    private val widgetRepository: WidgetRepository
) : GetWidgetsUseCase {

    override fun getFlow(): Flow<List<WidgetName>> = flow {
        widgetRepository.getAllWidgetsFlow().collect { entities ->
            if (entities.isEmpty()) {

                val defaultWidgetNamesList = getDefaultWidgetNames()
                widgetRepository.upsertWidgets(widgets = defaultWidgetNamesList.toDataModels())
                emit(defaultWidgetNamesList)

            } else {

                val widgets = entities.sortedBy { it.orderNum }.toDomainModels()
                emit(widgets)

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
package com.ataglance.walletglance.personalization.domain.usecase

import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.mapper.toDataModels

class SaveWidgetsUseCaseImpl(
    private val widgetRepository: WidgetRepository
) : SaveWidgetsUseCase {

    override suspend fun save(widgetsToSave: List<WidgetName>, currentWidgets: List<WidgetName>) {
        val entitiesToUpsert = widgetsToSave.toDataModels()
        val entitiesToDelete = currentWidgets
            .excludeItems(widgetsToSave) { it.name }
            .toDataModels()

        widgetRepository.deleteAndUpsertWidgets(
            toDelete = entitiesToDelete,
            toUpsert = entitiesToUpsert
        )
    }

    override suspend fun save(widgets: List<WidgetName>) {
        val currentWidgets = widgetRepository.getAllWidgets()

        val entitiesToUpsert = widgets.toDataModels()
        val entitiesToDelete = currentWidgets.excludeItems(entitiesToUpsert) { it.name }

        widgetRepository.deleteAndUpsertWidgets(
            toDelete = entitiesToDelete,
            toUpsert = entitiesToUpsert
        )
    }

}
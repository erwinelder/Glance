package com.ataglance.walletglance.navigation.mapper

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton


fun List<NavigationButtonEntity>.toDomainModels(): List<BottomBarNavigationButton> {
    return this.mapNotNull(NavigationButtonEntity::toDomainModel)
}

fun NavigationButtonEntity.toDomainModel(): BottomBarNavigationButton? {
    return this.screenName.getAppScreenEnum()?.toBottomBarNavigationButton()
}

fun String.getAppScreenEnum(): AppScreenEnum? {
    return when (this) {
        AppScreenEnum.Home.name -> AppScreenEnum.Home
        AppScreenEnum.Records.name -> AppScreenEnum.Records
        AppScreenEnum.CategoryStatistics.name -> AppScreenEnum.CategoryStatistics
        AppScreenEnum.Budgets.name -> AppScreenEnum.Budgets
        AppScreenEnum.Settings.name -> AppScreenEnum.Settings
        else -> null
    }
}

fun AppScreenEnum.toBottomBarNavigationButton(): BottomBarNavigationButton {
    return when (this) {
        AppScreenEnum.Home -> BottomBarNavigationButton.Home
        AppScreenEnum.Records -> BottomBarNavigationButton.Records
        AppScreenEnum.CategoryStatistics -> BottomBarNavigationButton.CategoryStatistics
        AppScreenEnum.Budgets -> BottomBarNavigationButton.Budgets
        AppScreenEnum.Settings -> BottomBarNavigationButton.Settings
    }
}


fun List<BottomBarNavigationButton>.toDataModelsWithDefaultOrderNums(): List<NavigationButtonEntity> {
    return this.map(BottomBarNavigationButton::toDataModelWithDefaultOrderNum)
}

fun BottomBarNavigationButton.toDataModelWithDefaultOrderNum(): NavigationButtonEntity {
    return when (this) {
        BottomBarNavigationButton.Home -> NavigationButtonEntity(AppScreenEnum.Home.name, 0)
        BottomBarNavigationButton.Records -> NavigationButtonEntity(AppScreenEnum.Records.name, 1)
        BottomBarNavigationButton.CategoryStatistics ->
            NavigationButtonEntity(AppScreenEnum.CategoryStatistics.name, 2)
        BottomBarNavigationButton.Budgets -> NavigationButtonEntity(AppScreenEnum.Budgets.name, 3)
        BottomBarNavigationButton.Settings -> NavigationButtonEntity(AppScreenEnum.Settings.name, 4)
    }
}


fun List<BottomBarNavigationButton>.toDataModels(): List<NavigationButtonEntity> {
    return this.mapIndexed { index, button -> button.toDataModel(index) }
}

fun BottomBarNavigationButton.toDataModel(orderNum: Int): NavigationButtonEntity {
    return when (this) {
        BottomBarNavigationButton.Home -> NavigationButtonEntity(AppScreenEnum.Home.name, orderNum)
        BottomBarNavigationButton.Records ->
            NavigationButtonEntity(AppScreenEnum.Records.name, orderNum)
        BottomBarNavigationButton.CategoryStatistics ->
            NavigationButtonEntity(AppScreenEnum.CategoryStatistics.name, orderNum)
        BottomBarNavigationButton.Budgets ->
            NavigationButtonEntity(AppScreenEnum.Budgets.name, orderNum)
        BottomBarNavigationButton.Settings ->
            NavigationButtonEntity(AppScreenEnum.Settings.name, orderNum)
    }
}

package com.ataglance.walletglance.navigation.domain.mapper

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButtons



fun NavigationButtonEntity.toBottomBarNavigationButton(): BottomBarNavigationButtons? {
    return this.screenName.getAppScreenEnum()?.toBottomBarNavigationButton()
}

fun List<NavigationButtonEntity>.toBottomBarNavigationButtonList():
        List<BottomBarNavigationButtons>
{
    return this.mapNotNull { it.toBottomBarNavigationButton() }
}



fun BottomBarNavigationButtons.toDefaultNavigationButtonEntity(): NavigationButtonEntity {
    return when (this) {
        BottomBarNavigationButtons.Home -> NavigationButtonEntity(AppScreenEnum.Home.name, 0)
        BottomBarNavigationButtons.Records -> NavigationButtonEntity(AppScreenEnum.Records.name, 1)
        BottomBarNavigationButtons.CategoryStatistics ->
            NavigationButtonEntity(AppScreenEnum.CategoryStatistics.name, 2)
        BottomBarNavigationButtons.Budgets -> NavigationButtonEntity(AppScreenEnum.Budgets.name, 3)
        BottomBarNavigationButtons.Settings -> NavigationButtonEntity(AppScreenEnum.Settings.name, 4)
    }
}

fun List<BottomBarNavigationButtons>.toDefaultNavigationButtonEntityList():
        List<NavigationButtonEntity>
{
    return this.map { it.toDefaultNavigationButtonEntity() }
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

fun AppScreenEnum.toBottomBarNavigationButton(): BottomBarNavigationButtons {
    return when (this) {
        AppScreenEnum.Home -> BottomBarNavigationButtons.Home
        AppScreenEnum.Records -> BottomBarNavigationButtons.Records
        AppScreenEnum.CategoryStatistics -> BottomBarNavigationButtons.CategoryStatistics
        AppScreenEnum.Budgets -> BottomBarNavigationButtons.Budgets
        AppScreenEnum.Settings -> BottomBarNavigationButtons.Settings
    }
}
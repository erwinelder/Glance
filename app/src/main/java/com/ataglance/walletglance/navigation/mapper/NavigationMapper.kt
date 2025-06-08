package com.ataglance.walletglance.navigation.mapper

import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState


fun List<NavigationButtonEntity>.toDomainModelsSorted(): List<AppScreenEnum> {
    return this
        .sortedBy { it.orderNum }
        .mapNotNull { entity ->
            enumValueOrNull<AppScreenEnum>(entity.screenName)
        }
}

fun List<AppScreenEnum>.toDataModels(): List<NavigationButtonEntity> {
    return this.mapIndexed { index, screen ->
        NavigationButtonEntity(screen.name, index)
    }
}


fun AppScreenEnum.toBottomBarNavButtonState(
    activeScreen: AppScreenEnum? = null
): BottomNavBarButtonState {
    return when (this) {
        AppScreenEnum.Home -> BottomNavBarButtonState.Home(
            isActive = this == activeScreen
        )
        AppScreenEnum.Records -> BottomNavBarButtonState.Records(
            isActive = this == activeScreen
        )
        AppScreenEnum.CategoryStatistics -> BottomNavBarButtonState.CategoryStatistics(
            isActive = this == activeScreen
        )
        AppScreenEnum.Budgets -> BottomNavBarButtonState.Budgets(
            isActive = this == activeScreen
        )
        AppScreenEnum.Settings -> BottomNavBarButtonState.Settings(
            isActive = this == activeScreen
        )
    }
}

fun BottomNavBarButtonState.toAppScreenEnum(): AppScreenEnum? {
    return when (this) {
        is BottomNavBarButtonState.Home -> AppScreenEnum.Home
        is BottomNavBarButtonState.Records -> AppScreenEnum.Records
        is BottomNavBarButtonState.CategoryStatistics -> AppScreenEnum.CategoryStatistics
        is BottomNavBarButtonState.Budgets -> AppScreenEnum.Budgets
        is BottomNavBarButtonState.Settings -> AppScreenEnum.Settings
        is BottomNavBarButtonState.Other -> null
    }
}

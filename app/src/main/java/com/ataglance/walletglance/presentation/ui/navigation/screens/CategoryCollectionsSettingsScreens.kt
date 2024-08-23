package com.ataglance.walletglance.presentation.ui.navigation.screens

import kotlinx.serialization.Serializable

sealed interface CategoryCollectionsSettingsScreens {

    @Serializable
    data object EditCategoryCollections : CategoryCollectionsSettingsScreens

    @Serializable
    data object EditCategoryCollection : CategoryCollectionsSettingsScreens

}
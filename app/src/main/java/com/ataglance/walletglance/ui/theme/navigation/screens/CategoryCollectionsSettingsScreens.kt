package com.ataglance.walletglance.ui.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface CategoryCollectionsSettingsScreens {

    @Serializable
    data object EditCategoryCollections : CategoryCollectionsSettingsScreens

    @Serializable
    data object EditCategoryCollection : CategoryCollectionsSettingsScreens

}
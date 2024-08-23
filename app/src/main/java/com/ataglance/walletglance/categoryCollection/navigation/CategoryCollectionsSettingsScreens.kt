package com.ataglance.walletglance.categoryCollection.navigation

import kotlinx.serialization.Serializable

sealed interface CategoryCollectionsSettingsScreens {

    @Serializable
    data object EditCategoryCollections : CategoryCollectionsSettingsScreens

    @Serializable
    data object EditCategoryCollection : CategoryCollectionsSettingsScreens

}
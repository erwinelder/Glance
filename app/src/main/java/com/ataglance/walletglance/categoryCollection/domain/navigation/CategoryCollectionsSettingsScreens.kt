package com.ataglance.walletglance.categoryCollection.domain.navigation

import kotlinx.serialization.Serializable

sealed interface CategoryCollectionsSettingsScreens {

    @Serializable
    data object EditCategoryCollections : CategoryCollectionsSettingsScreens

    @Serializable
    data object EditCategoryCollection : CategoryCollectionsSettingsScreens

}
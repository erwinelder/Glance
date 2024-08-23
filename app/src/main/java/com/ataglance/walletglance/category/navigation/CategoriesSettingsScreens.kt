package com.ataglance.walletglance.category.navigation

import kotlinx.serialization.Serializable

sealed interface CategoriesSettingsScreens {

    @Serializable
    data object EditCategories : CategoriesSettingsScreens

    @Serializable
    data object EditSubcategories : CategoriesSettingsScreens

    @Serializable
    data object EditCategory : CategoriesSettingsScreens

}
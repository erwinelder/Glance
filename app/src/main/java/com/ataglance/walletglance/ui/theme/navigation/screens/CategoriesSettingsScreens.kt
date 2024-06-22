package com.ataglance.walletglance.ui.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface CategoriesSettingsScreens {

    @Serializable
    data object EditCategories : CategoriesSettingsScreens

    @Serializable
    data object EditSubcategories : CategoriesSettingsScreens

    @Serializable
    data object EditCategory : CategoriesSettingsScreens

}